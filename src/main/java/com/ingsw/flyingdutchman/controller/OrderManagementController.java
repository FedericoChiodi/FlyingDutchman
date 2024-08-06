package com.ingsw.flyingdutchman.controller;


import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.AuctionService;
import com.ingsw.flyingdutchman.model.service.OrderService;
import com.ingsw.flyingdutchman.model.service.ProductService;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orderManagement")
public class OrderManagementController {

    private final OrderService orderService;
    private final UserService userService;
    private final AuctionService auctionService;
    private final ProductService productService;

    @Autowired
    private OrderManagementController(OrderService orderService, UserService userService, AuctionService auctionService, ProductService productService){
        this.orderService = orderService;
        this.userService = userService;
        this.auctionService = auctionService;
        this.productService = productService;
    }

    @GetMapping("/view")
    public String view(HttpServletRequest request) {
        User loggedUser = userService.findLoggedUser(request);
        List<Order> orders = orderService.findOrdersByUser(loggedUser);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("orders",orders);

        return "orderManagement/view";
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        Auction auction = auctionService.findAuctionById(Long.parseLong(request.getParameter("auctionId")));
        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        auction.setClosing_timestamp(timestamp);
        auction.setProduct_sold('Y');

        auctionService.updateAuction(auction);

        orderService.createOrder(
                timestamp,
                auction.getProduct_auctioned().getCurrent_price(),
                'N',
                loggedUser,
                auction.getProduct_auctioned()
        );

        request.setAttribute("applicationMessage","Il tuo ordine è stato inserito!");

        List<Order> orders = orderService.findOrdersByUser(loggedUser);

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser",loggedUser);
        request.setAttribute("orders",orders);

        return "orderManagement/view";
    }

    @PostMapping("/premium")
    public String buyPremium(HttpServletRequest request, HttpServletResponse response){
        User loggedUser = userService.findLoggedUser(request);

        loggedUser.setRole("Premium");

        userService.updateUser(loggedUser);

        // Could this be removed?
        userService.createLoginCookie(loggedUser, response);

        Product premium = productService.findProductById(1L);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        orderService.createOrder(
                timestamp,
                premium.getCurrent_price(),
                'N',
                loggedUser,
                premium
        );

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser",loggedUser);

        return "orderManagement/premium";
    }

    @GetMapping("/premium")
    public String buyPremiumView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(1L);

        request.setAttribute("loggedOn",loggedUser != null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("auction", auction);
        request.setAttribute("isPremium", true);

        return "orderManagement/insertView";
    }

}
