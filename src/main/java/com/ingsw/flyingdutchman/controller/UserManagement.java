package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

public class UserManagement {
    private UserManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","userManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / view", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void delete(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            loggedUser = userDAO.findByUsername(loggedUser.getUsername());

            Product[] products = daoFactory.getProductDAO().findByOwner(loggedUser);
            Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(loggedUser);
            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);

            try {
                sessionUserDAO.delete(loggedUser);
                userDAO.delete(loggedUser);
                for (Product product : products) {
                    daoFactory.getProductDAO().delete(product);
                }
                for (Auction auction : auctions){
                    daoFactory.getAuctionDAO().delete(auction);
                }
                for (Threshold threshold : thresholds){
                    daoFactory.getThresholdDAO().delete(threshold);
                }
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Errore cancellazione utente e relativi dati collegati. " + e);
            }

            //Elimino tutte le immagini dell'utente
            String directoryPath = "/home/sanpc/tomcat/webapps/Uploads/" + loggedUser.getUsername();
            File directory = new File(directoryPath);

            if (directory.isDirectory()) {
                String[] entries = directory.list();
                for(String s: entries){
                    File currentFile = new File(directory.getPath(),s);
                    currentFile.delete();
                }
                directory.delete();
            }
            else {
                System.out.println("Non ho cancellato nulla al path: " + directoryPath);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser",null);
            request.setAttribute("applicationMessage","Account correttamente eliminato. Arrivederci!");
            request.setAttribute("viewUrl","homeManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / delete", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void insert(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            try {
                userDAO.create(
                        request.getParameter("username"),
                        request.getParameter("password"),
                        request.getParameter("firstname"),
                        request.getParameter("surname"),
                        Date.valueOf(request.getParameter("birthdate")),
                        request.getParameter("address"),
                        Short.parseShort(request.getParameter("civic_number")),
                        Short.parseShort(request.getParameter("cap")),
                        request.getParameter("city"),
                        request.getParameter("state"),
                        request.getParameter("email"),
                        request.getParameter("cel_number"),
                        request.getParameter("role"),
                        request.getParameter("deleted")
                );
                applicationMessage = "Account creato correttamente! Loggati per iniziare";
            }catch (Exception e){
                applicationMessage = "Utente non creato, username gia' in uso!";
                logger.log(Level.SEVERE, "Errore nella creazione dell'utente: " + e);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","homeManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / insert", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void insertView(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();
        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("viewUrl","userManagement/insModView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / insView", e);
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void modifyView(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();
        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            loggedUser = userDAO.findByUsername(loggedUser.getUsername());

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auctionID",request.getParameter("auctionID"));
            request.setAttribute("viewUrl","userManagement/insModView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / modView", e);
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void modify(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(loggedUser.getUsername());

            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setFirstname(request.getParameter("firstname"));
            user.setSurname(request.getParameter("surname"));
            user.setBirthdate(Date.valueOf(request.getParameter("birthdate")));
            user.setAddress(request.getParameter("address"));
            user.setCivic_number(Short.parseShort(request.getParameter("civic_number")));
            user.setCap(Short.parseShort(request.getParameter("cap")));
            user.setCity(request.getParameter("city"));
            user.setState(request.getParameter("state"));
            user.setEmail(request.getParameter("email"));
            user.setCel_number(request.getParameter("cel_number"));
            user.setRole(request.getParameter("role"));
            user.setDeleted(request.getParameter("deleted").equals("Y"));

            try {
                userDAO.update(user);
            }
            catch (Exception e){
                applicationMessage = "Errore nell'aggiornamento dei dati utente";
            }

            //Ricordo a che asta devo tornare
            String auctionID = request.getParameter("auctionID");

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser!=null);
            request.setAttribute("loggedUser",user);
            request.setAttribute("applicationMessage",applicationMessage);
            if(Long.parseLong(auctionID) < 1){
                request.setAttribute("viewUrl","userManagement/view");
            }
            else {
                Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(auctionID));
                Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
                product.setOwner(owner);
                auction.setProduct_auctioned(product);
                request.setAttribute("auction",auction);
                request.setAttribute("viewUrl","auctionManagement/inspectAuction");
            }
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / modify", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void banView(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            loggedUser = userDAO.findByUsername(loggedUser.getUsername());

            User[] usernames = userDAO.findAllUsersExceptMeAndDeleted(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("usernames",usernames);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","userManagement/banView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / modify", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
    public static void ban(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            loggedUser = userDAO.findByUsername(loggedUser.getUsername());

            User toBan = userDAO.findByUsername(request.getParameter("username"));

            if(toBan.getUserID() != null){
                Product[] products = daoFactory.getProductDAO().findByOwner(toBan);
                Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(toBan);
                Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(toBan);

                try {
                    userDAO.delete(toBan);
                    for (Product product : products) {
                        daoFactory.getProductDAO().delete(product);
                    }
                    for (Auction auction : auctions){
                        daoFactory.getAuctionDAO().delete(auction);
                    }
                    for (Threshold threshold : thresholds){
                        daoFactory.getThresholdDAO().delete(threshold);
                    }
                }
                catch (Exception e){
                    logger.log(Level.SEVERE, "Errore cancellazione utente e relativi dati collegati. " + e);
                    throw new RuntimeException(e);
                }

                applicationMessage = "Utente bannato correttamente";
            }
            else {
                applicationMessage = "Username inserito non trovato!";
            }

            //Elimino tutte le immagini dell'utente
            String directoryPath = "/home/sanpc/tomcat/webapps/Uploads/" + toBan.getUsername();
            File directory = new File(directoryPath);

            if (directory.isDirectory()) {
                String[] entries = directory.list();
                for(String s: entries){
                    File currentFile = new File(directory.getPath(),s);
                    currentFile.delete();
                }
                directory.delete();
            }
            else {
                System.out.println("Non ho cancellato nulla al path: " + directoryPath);
            }

            User[] usernames = userDAO.findAllUsersExceptMeAndDeleted(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("usernames",usernames);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","userManagement/banView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / ban", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }


}
