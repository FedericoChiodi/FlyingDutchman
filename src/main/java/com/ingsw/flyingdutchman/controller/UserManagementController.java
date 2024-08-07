package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.AuctionService;
import com.ingsw.flyingdutchman.model.service.ProductService;
import com.ingsw.flyingdutchman.model.service.ThresholdService;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/userManagement")
public class UserManagementController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final ProductService productService;
    private final ThresholdService thresholdService;

    @Autowired
    public UserManagementController(UserService userService, AuctionService auctionService, ProductService productService, ThresholdService thresholdService) {
        this.userService = userService;
        this.auctionService = auctionService;
        this.productService = productService;
        this.thresholdService = thresholdService;
    }

    @GetMapping("/view")
    public String view(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn", loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        return "userManagement/view";
    }

    @PostMapping("/ban")
    public String delete(HttpServletRequest request, HttpServletResponse response){
        User loggedUser = userService.findLoggedUser(request);

        if(loggedUser != null){
            // Elimina tutte le prenotazioni dell'utente
            List<Threshold> thresholdList = thresholdService.findThresholdsByUser(loggedUser);
            for(Threshold threshold : thresholdList){
                thresholdService.deleteThreshold(threshold);
            }

            // Elimina tutti i prodotti dell'utente
            List<Product> productList = productService.findAllProducts();
            for(Product product : productList){
                productService.deleteProduct(product);
            }

            // Elimina tutte le aste dell'utente
            List<Auction> auctionList = auctionService.findAllAuctions();
            for(Auction auction : auctionList){
                auctionService.deleteAuction(auction);
            }

            // Elimina tutte le immagini dell'utente
            String directoryPath = "/home/sanpc/tomcat/webapps/Uploads/" + loggedUser.getUsername();
            File directory = new File(directoryPath);

            if (directory.isDirectory()) {
                String[] entries = directory.list();
                assert entries != null;
                for(String s: entries){
                    File currentFile = new File(directory.getPath(),s);
                    currentFile.delete();
                }
                directory.delete();
            }
            else {
                System.out.println("Non ho cancellato nulla al path: " + directoryPath);
            }

            // Elimina l'utente e il cookie di login
            userService.deleteUser(loggedUser);
            userService.deleteLoginCookie(response);

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser",null);
            request.setAttribute("applicationMessage","Account correttamente eliminato. Arrivederci!");
        }

        return "homeManagement/view";
    }

    @GetMapping("/ban")
    public String banView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        List<User> usernames = userService.findAllUsersExceptMeAndDeleted(loggedUser);

        request.setAttribute("loggedOn", loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("usernames",usernames);

        return "userManagement/banView";
    }

    @PostMapping("/insert")
    public String insertUser(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        String applicationMessage;
        try {
            userService.create(
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
                    request.getParameter("role")
            );
            applicationMessage = "Account creato correttamente!";
        }
        catch (Exception e){
            applicationMessage = "Username già esistente!";
        }

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("applicationMessage", applicationMessage);

        return "homeManagement/view";
    }

    @GetMapping("/insert")
    public String insertView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);

        return "userManagement/insModView";
    }

    @PostMapping("/modify")
    public String modifyUser(HttpServletRequest request){
        User user = userService.findLoggedUser(request);

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
        user.setDeleted(request.getParameter("deleted").equals("Y") ? 'Y' : 'N');

        userService.updateUser(user);

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", user);

        String auctionID = request.getParameter("auctionID");
        if(auctionID != null){
            Auction auction = auctionService.findAuctionById(Long.valueOf(auctionID));

            request.setAttribute("auction",auction);
            return "auctionManagement/inspectAuction";
        }
        else{
            return "homeManagement/view";
        }
    }

    @GetMapping("/modify")
    public String modifyView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("auctionID",request.getParameter("auctionID"));

        return "userManagement/insModView";
    }
}