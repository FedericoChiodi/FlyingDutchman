package com.ingsw.flyingdutchman.services;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@WebServlet("/Upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String controllerAction = request.getParameter("controllerAction");

            if(controllerAction==null) controllerAction="HomeManagement.view";

            String[] splitAction = controllerAction.split("\\.");
            Class<?> controllerClass = Class.forName("com.ingsw.flyingdutchman.controller."+splitAction[0]);
            Method controllerMethod = controllerClass.getMethod(splitAction[1], HttpServletRequest.class, HttpServletResponse.class);
            LogService.getApplicationLogger().log(Level.INFO, splitAction[0]+" "+splitAction[1]);
            controllerMethod.invoke(null,request,response);

            String viewUrl = (String) request.getAttribute("viewUrl");
            RequestDispatcher view = request.getRequestDispatcher("jsp/"+ viewUrl + ".jsp");
            view.forward(request,response);
        }
        catch (Exception e){
            e.printStackTrace(out);
            throw new ServerException("Dispatcher Servlet Error.");
        }
        finally {
            out.close();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request",request);
        sessionFactoryParameters.put("response",response);
        DAOFactory sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
        sessionDAOFactory.beginTransaction();

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();


        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("image"); // Retrieves <input type="file" name="image">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();

        // Specifica il percorso della cartella in cui desideri salvare il file
        String uploadDirPath = "/home/sanpc/tomcat/webapps/Uploads/" + loggedUser.getUsername();

        // Crea la cartella se non esiste
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Crea il percorso completo del file di destinazione
        String[] tokens = fileName.split("\\.(?=[^\\.]+$)"); // regex magico che estrae il formato del file --> [nomeFile.a.b.c],[png]
        String filePath = uploadDirPath  +  File.separator  + description + "." + tokens[1];

        // Salva il file nel percorso specificato
        Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        try {
            sessionDAOFactory.commitTransaction();
        }
        catch (Exception e){
            try {
                sessionDAOFactory.rollbackTransaction();
            }catch (Throwable t){}
        }
        finally {
            sessionDAOFactory.closeTransaction();
        }

        processRequest(request,response);
    }
}