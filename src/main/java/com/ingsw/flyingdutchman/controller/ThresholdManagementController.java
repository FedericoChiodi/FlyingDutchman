package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/thresholdManagement")
public class ThresholdManagementController {

    private final ThresholdService thresholdService;
    private final UserService userService;
    private final AuctionService auctionService;
    private final OrderService orderService;
    private final CategoryService categoryService;

    // Percentuali massime e minime di ribasso automatico
    private static final int max = 20;
    private static final int min = 5;

    @Autowired
    public ThresholdManagementController(ThresholdService thresholdService, UserService userService, AuctionService auctionService, OrderService orderService, CategoryService categoryService) {
        this.thresholdService = thresholdService;
        this.userService = userService;
        this.auctionService = auctionService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    @GetMapping("/view")
    public String view(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        List<Threshold> thresholds = thresholdService.findThresholdsByUser(loggedUser);

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("thresholds", thresholds);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/view";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Threshold threshold = thresholdService.findThresholdById(Long.valueOf(request.getParameter("thresholdID")));

        try{
            thresholdService.deleteThreshold(threshold);
            request.setAttribute("applicationMessage", "Eliminata correttamente!");
        }
        catch (Exception e){
            request.setAttribute("applicationMessage", "Could not delete threshold!");
        }

        List<Threshold> thresholds = thresholdService.findThresholdsByUser(loggedUser);

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("thresholds", thresholds);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/view";
    }

    @PostMapping("/insert")
    public String insert(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        try {
            thresholdService.createThreshold(
                    Float.parseFloat(request.getParameter("price")),
                    timestamp,
                    loggedUser,
                    auction
            );
        }
        catch (Exception e){
            request.setAttribute("applicationMessage", "Could not insert threshold!");
        }

        List<Threshold> thresholds = thresholdService.findThresholdsByUser(loggedUser);

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("thresholds", thresholds);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/view";
    }

    @GetMapping("/insert")
    public String insertView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("auction", auction);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/insModView";
    }

    @PostMapping("/modify")
    public String modify(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Threshold threshold = thresholdService.findThresholdById(Long.valueOf(request.getParameter("thresholdID")));

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        threshold.setPrice(Float.parseFloat(request.getParameter("price")));
        threshold.setReservationDate(timestamp);

        try{
            thresholdService.updateThreshold(threshold);
        }
        catch (Exception e){
            request.setAttribute("applicationMessage","Could not update threshold!");
        }

        List<Threshold> thresholds = thresholdService.findThresholdsByUser(loggedUser);

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("thresholds", thresholds);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/view";
    }

    @GetMapping("/modify")
    public String modifyView(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);
        Threshold threshold = thresholdService.findThresholdById(Long.valueOf(request.getParameter("thresholdID")));

        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("loggedOn", true);
        request.setAttribute("threshold", threshold);
        request.setAttribute("menuActiveLink", "Prenota");

        return "thresholdManagement/insModView";
    }

    @RequestMapping("/update")
    public String checkOnUpdate(HttpServletRequest request){
        User loggedUser = userService.findLoggedUser(request);

        //Pagina a cui devo ritornare dopo i controlli
        String pageToReturn = request.getParameter("pageToReturn");

        if(pageToReturn.equals("auctionManagement/view")){
            Auction auction = auctionService.findAuctionById(Long.valueOf(request.getParameter("auctionID")));
            auction.getProduct_auctioned().setCurrent_price(Float.parseFloat(request.getParameter("price")));

            try{
                auctionService.updateAuction(auction);
                request.setAttribute("applicationMessage", "Prezzo abbassato correttamente!");
            }
            catch (Exception e){
                request.setAttribute("applicationMessage", "Could not lower price!");
            }

            checkPrices(auction);
        }

        if(pageToReturn.equals("auctionManagement/lowerAllView")){
            // Dummy user per riciclare un metodo
            User user = new User();
            user.setUserID((long) -1);
            List<Auction> auctions = auctionService.findAllOpenAuctionsExceptUser(user);

            for(Auction auction : auctions){
                float percentageToLower_gen = generateRandom();
                float priceLowered = auction.getProduct_auctioned().getCurrent_price() - (auction.getProduct_auctioned().getCurrent_price() * percentageToLower_gen);

                // Il prezzo abbassato è superiore al prezzo minimo
                if(priceLowered > auction.getProduct_auctioned().getMin_price()){
                    auction.getProduct_auctioned().setCurrent_price(priceLowered);
                }
                else{
                    auction.getProduct_auctioned().setCurrent_price(auction.getProduct_auctioned().getMin_price());
                }

                try{
                    auctionService.updateAuction(auction);
                    request.setAttribute("applicationMessage","Prezzi aggiornati in " + auctions.size() + " aste!");
                }
                catch (Exception e){
                    request.setAttribute("applicationMessage","Could not lower prices!");
                }
            }

            for (Auction auction : auctions) {
                checkPrices(auction);
            }
        }

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("menuActiveLink", "Prenota");

        if(pageToReturn.equals("auctionManagement/view")){
            List<Auction> auctions = auctionService.findOpenAuctionsByOwnerNotDeleted(loggedUser);
            List<Category> categories = categoryService.getAllCategoriesExceptPremium();

            request.setAttribute("canEdit",true);
            request.setAttribute("auctions", auctions);
            request.setAttribute("categories", categories);
        }

        return pageToReturn;
    }

    public void checkPrices(Auction auction) {
        List<Threshold> thresholds = thresholdService.findThresholdsByAuction(auction);
        if(!thresholds.isEmpty()){
            List<Threshold> validThresholds = new ArrayList<>();
            for(Threshold threshold : thresholds){
                if(threshold.getPrice() >= auction.getProduct_auctioned().getCurrent_price()){
                    validThresholds.add(threshold);
                }
            }
            if(!validThresholds.isEmpty()){
                createOrderFromThreshold(auction, validThresholds);
            }
        }
    }

    public void createOrderFromThreshold(Auction auction, List<Threshold> validThresholds) {
        // Si trova la prenotazione da ordinare:
        // Quella con prezzo più alto. In caso di parità si
        // sceglie quella piazzata prima cronologicamente
        Threshold toOrder = validThresholds.get(0);
        for (Threshold threshold : validThresholds) {
            if (threshold.getPrice() > toOrder.getPrice()) {
                toOrder = threshold;
            } else if (Objects.equals(threshold.getPrice(), toOrder.getPrice())) {
                if (threshold.getReservationDate().compareTo(toOrder.getReservationDate()) < 0) {
                    toOrder = threshold;
                }
            }
        }

        Float fallback_price = auction.getProduct_auctioned().getCurrent_price();
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            auction.setClosing_timestamp(timestamp);
            auction.setProduct_sold('Y');
            auction.getProduct_auctioned().setCurrent_price(toOrder.getPrice());

            auctionService.updateAuction(auction);
            try {
                orderService.createOrder(
                        timestamp,
                        toOrder.getPrice(),
                        'Y',
                        toOrder.getUser(),
                        auction.getProduct_auctioned()
                );
                for (Threshold threshold : validThresholds) {
                    try {
                        thresholdService.deleteThreshold(threshold);
                    } catch (Exception e) {
                        System.err.println("Could not delete threshold: " + threshold);
                    }
                }
            } catch (Exception e) {
                System.err.println("Could not create order from: " + toOrder);
            }
        } catch (Exception e) {
            auction.setClosing_timestamp(null);
            auction.setProduct_sold('N');
            auction.getProduct_auctioned().setCurrent_price(fallback_price);

            System.err.println("Could not update auction: " + auction);
        }
    }

    public float generateRandom(){
        int randomNumber = (int) (Math.random() * (max - min + 1)) + min;
        System.out.println(randomNumber / 100f);
        return (randomNumber / 100f);
    }
}