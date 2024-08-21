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
        request.setAttribute("menuActiveLink", "Utente");

        return "userManagement/view";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response){
        User loggedUser = userService.findLoggedUser(request);

        String directoryPath = "/home/sanpc/uploads/" + loggedUser.getUsername();
        File directory = new File(directoryPath);

        delete_user(loggedUser, directory);

        userService.deleteLoginCookie(response);

        request.setAttribute("loggedOn",false);
        request.setAttribute("loggedUser",null);
        request.setAttribute("applicationMessage","Account correttamente eliminato. Arrivederci!");

        return "homeManagement/view";
    }

    @PostMapping("/ban")
    public String ban(HttpServletRequest request){
        User loggeduser = userService.findLoggedUser(request);
        User toDelete = userService.findByUsername(request.getParameter("username"));

        String directoryPath = "/home/sanpc/uploads/" + toDelete.getUsername();
        File directory = new File(directoryPath);

        delete_user(toDelete, directory);

        request.setAttribute("loggedOn",true);
        request.setAttribute("loggedUser",loggeduser);
        request.setAttribute("applicationMessage","Bannato: " + toDelete.getUsername());

        return "homeManagement/view";
    }

    @GetMapping("/ban")
    public String banView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        List<User> usernames = userService.findAllUsersExceptMeAndDeleted(loggedUser);

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("usernames",usernames);
        request.setAttribute("menuActiveLink", "Banna");

        return "userManagement/banView";
    }

    @RequestMapping("/register")
    public String insert(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

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
            request.setAttribute("applicationMessage", "Account creato correttamente!");
        }
        catch (Exception e){
            request.setAttribute("applicationMessage", "Username già esistente!");
        }

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("menuActiveLink", "Utente");

        return "homeManagement/view";
    }

    @GetMapping("/insert")
    public String insertView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("menuActiveLink", "Utente");

        return "userManagement/insModView";
    }

    @PostMapping("/modify")
    public String modify(HttpServletRequest request){
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
        user.setDeleted(request.getParameter("deleted").charAt(0));

        try{
            userService.updateUser(user);
        }
        catch (Exception e){
            request.setAttribute("applicationMessage", "Could not update User!");
        }

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", user);
        request.setAttribute("menuActiveLink", "Utente");

        String auctionID = request.getParameter("auctionID");
        try{
            Auction auction = auctionService.findAuctionById(Long.valueOf(auctionID));
            request.setAttribute("auction",auction);
            return "auctionManagement/inspectAuction";

        }
        catch (Exception e) {
            return "userManagement/view";
        }
    }

    @GetMapping("/modify")
    public String modifyView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("auctionID",request.getParameter("auctionID"));
        request.setAttribute("menuActiveLink", "Utente");

        return "userManagement/insModView";
    }

    public void delete_user(User user, File directory){
        // Elimina tutte le prenotazioni dell'utente
        List<Threshold> thresholdList = thresholdService.findThresholdsByUser(user);
        for(Threshold threshold : thresholdList){
            try{
                thresholdService.deleteThreshold(threshold);
            }
            catch (Exception e){
                System.err.println("Could not delete threshold: " + threshold);
            }
        }

        // Elimina tutti i prodotti dell'utente
        List<Product> productList = productService.findProductsByOwner(user);
        for(Product product : productList){
            try {
                productService.deleteProduct(product);
            }
            catch (Exception e){
                System.err.println("Could not delete product: " + product);
            }
        }

        // Elimina tutte le aste dell'utente
        List<Auction> auctionList = auctionService.findByOwner(user);
        for(Auction auction : auctionList){
            try {
                auctionService.deleteAuction(auction);
            }
            catch (Exception e){
                System.err.println("Could not delete auction: " + auction);
            }
        }

        // Elimina tutte le immagini dell'utente
        File[] files = directory.listFiles();
        if(files != null){
            for(File file: files){
                if(!file.delete())
                    System.err.println("Could not delete file: " + file.getAbsolutePath());
            }
            if(!directory.delete())
                System.err.println("Could not delete directory: " + directory.getAbsolutePath());
        }

        // Elimina l'utente
        try{
            userService.deleteUser(user);
        }
        catch (Exception e){
            System.err.println("Could not delete user: " + user);
        }
    }

}