package com.ingsw.flyingdutchman.controller;


import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

@Controller
@RequestMapping("/productManagement")
public class ProductManagementController {

    private final ProductService productService;
    private final UserService userService;
    private final AuctionService auctionService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    @Autowired
    private ProductManagementController(ProductService productService, UserService userService, AuctionService auctionService, CategoryService categoryService, OrderService orderService){
        this.productService = productService;
        this.userService = userService;
        this.auctionService = auctionService;
        this.categoryService = categoryService;
        this.orderService = orderService;
    }

    @GetMapping("/view")
    public String view(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        return prepareProducts(request, loggedUser);
    }

    @GetMapping("/insertView")
    public String insertView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Category> categories = categoryService.getAllCategoriesExceptPremium();

        request.setAttribute("loggedOn", loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("categories", categories);
        request.setAttribute("menuActiveLink", "Prodotti");

        return "productManagement/insertView";
    }

    @PostMapping("/insert")
    public String insert(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        String imagePath = "/home/sanpc/tomcat/webapps/Uploads/" + loggedUser.getUsername() + File.separator + request.getParameter("description") + ".png";
        productService.createProduct(
                request.getParameter("description"),
                Float.parseFloat(request.getParameter("min_price")),
                Float.parseFloat(request.getParameter("starting_price")),
                Float.parseFloat(request.getParameter("current_price")),
                imagePath,
                categoryService.findCategoryById(Long.valueOf(request.getParameter("categoryID"))),
                loggedUser
        );

        List<Product> products = productService.findProductByOwnerNotDeletedNotSold(loggedUser);

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("products", products);
        request.setAttribute("soldProductsAction", false);
        request.setAttribute("menuActiveLink", "Prodotti");

        return "productManagement/view";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Product product = productService.findProductById(Long.valueOf(request.getParameter("productID")));
        List<Auction> auctions = auctionService.findByProductOpenNotDeleted(product);

        if(!auctions.isEmpty()){
            request.setAttribute("applicationMessage", "Non puoi eliminare un prodotto attualmente in asta!");
        }
        else{
            productService.deleteProduct(product);
        }

        return prepareProducts(request, loggedUser);
    }

    @RequestMapping("/viewSold")
    public String viewSoldProducts(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Product> products = productService.findProductByOwnerNotDeletedNotSold(loggedUser);
        List<User> buyers = new ArrayList<>();

        for(Product product : products){
            Order order = orderService.findOrderByProduct(product);
            buyers.add(userService.findByUserID(order.getBuyer().getUserID()));
        }

        request.setAttribute("loggedOn", loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("products", products);
        request.setAttribute("buyers", buyers);
        request.setAttribute("soldProductsAction", true);
        request.setAttribute("menuActiveLink", "Prodotti");

        return "productManagement/view";
    }

    @NotNull
    private String prepareProducts(HttpServletRequest request, User loggedUser) {
        List<Product> products = productService.findProductByOwnerNotDeletedNotSold(loggedUser);

        request.setAttribute("loggedOn", loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("products", products);
        request.setAttribute("soldProductsAction", false);
        request.setAttribute("menuActiveLink", "Prodotti");

        return "productManagement/view";
    }
}