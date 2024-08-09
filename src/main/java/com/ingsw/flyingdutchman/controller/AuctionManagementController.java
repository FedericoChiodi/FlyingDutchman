package com.ingsw.flyingdutchman.controller;


import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/auctionManagement")
public class AuctionManagementController {

    private final AuctionService auctionService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ThresholdService thresholdService;

    @Autowired
    private AuctionManagementController(AuctionService auctionService, UserService userService, CategoryService categoryService, ProductService productService, ThresholdService thresholdService) {
        this.auctionService = auctionService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.thresholdService = thresholdService;
    }

    @GetMapping("/view")
    public String view(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Auction> auctions = auctionService.findAllOpenAuctionsExceptUser(loggedUser);
        return prepareRequestCategory(request, loggedUser, auctions);
    }

    @GetMapping("/myAuctions")
    public String viewMyAuctions(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Auction> auctions = auctionService.findOpenAuctionsByOwnerNotDeleted(loggedUser);
        return prepareRequestCategoryEdit(request, loggedUser, auctions);
    }

    @GetMapping("/insert")
    public String insertView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Product> products = productService.findProductByOwnerNotDeletedNotSoldNotInAuction(loggedUser);

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("products", products);

        return "auctionManagement/insertView";
    }

    @PostMapping("/insert")
    public String insert(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Product product = productService.findProductByIdNotDeletedNotSoldNotInAuction(Long.parseLong(request.getParameter("productID")));

        if(product == null){
            request.setAttribute("applicationMessage","Prodotto già all'asta!");
        }
        else{
            auctionService.createAuction(
                    Timestamp.valueOf(request.getParameter("opening_timestamp")),
                    product
            );
            request.setAttribute("applicationMessage","Asta creata correttamente!");
        }

        List<Auction> auctions = auctionService.findAllOpenAuctionsExceptUser(loggedUser);
        return prepareRequestCategory(request, loggedUser, auctions);
    }

    @GetMapping({"/inspect","/buyProduct","/update"})
    public String inspectAuction(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("auction",auction);

        String uri = request.getRequestURI();
        if(uri.endsWith("inspect")){
            return "auctionManagement/inspectAuction";
        }
        else if(uri.endsWith("buyProduct")){
            return "orderManagement/insertView";
        }
        else{
            return "auctionManagement/modifyPriceView";
        }
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));

        auction.getProduct_auctioned().setCurrent_price(Float.parseFloat(request.getParameter("price")));

        auctionService.updateAuction(auction);

        return prepareRequestAuctions(request, loggedUser);
    }

    @GetMapping("/lowerAll")
    public String lowerAllView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser",loggedUser);

        return "auctionManagement/lowerAllView";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));
        List<Threshold> thresholds = thresholdService.findThresholdsByAuction(auction);

        for (Threshold threshold : thresholds){
            thresholdService.deleteThreshold(threshold);
        }

        return prepareRequestAuctions(request, loggedUser);
    }

    @RequestMapping("/search")
    public String search(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        List<Auction> auctions = auctionService.findAuctionByProductDescription(request.getParameter("auctionName"));
        return prepareRequestCategory(request, loggedUser, auctions);
    }

    @RequestMapping("/searchCategory")
    public String searchCategory(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        Category category = categoryService.findCategoryById(Long.valueOf(request.getParameter("categoryID")));
        List<Auction> auctions = auctionService.findAuctionsByCategory(category);
        return prepareRequestCategory(request, loggedUser, auctions);
    }

    @NotNull
    private String prepareRequestCategory(HttpServletRequest request, User loggedUser, List<Auction> auctions) {
        List<Category> categories = categoryService.getAllCategoriesExceptPremium();

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("auctions", auctions);
        request.setAttribute("categories",categories);

        return "auctionManagement/view";
    }

    @NotNull
    private String prepareRequestAuctions(HttpServletRequest request, User loggedUser) {
        List<Auction> auctions = auctionService.findAllOpenAuctionsExceptUser(loggedUser);
        return prepareRequestCategoryEdit(request, loggedUser, auctions);
    }

    @NotNull
    private String prepareRequestCategoryEdit(HttpServletRequest request, User loggedUser, List<Auction> auctions) {
        List<Category> categories = categoryService.getAllCategoriesExceptPremium();

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("auctions", auctions);
        request.setAttribute("categories",categories);
        request.setAttribute("canEdit", true);

        return "auctionManagement/view";
    }
}