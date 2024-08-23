package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(OrderManagementController.class)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @Test
    public void view_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        List<Order> orders = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(orderService.findOrdersByUser(loggedUser)).thenReturn(orders);

        mockMvc.perform(get("/orderManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("orders", orders))
                .andExpect(request().attribute("menuActiveLink", "Ordini"));
    }

    @Test
    public void pay_test() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(1L);

        Auction mockAuction = new Auction();
        mockAuction.setAuctionID(1L);
        mockAuction.setProduct_sold('N');
        Product mockProduct = new Product();
        mockProduct.setCurrent_price(100f);
        mockAuction.setProduct_auctioned(mockProduct);

        List<Order> mockOrders = Collections.emptyList();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(auctionService.findAuctionById(any(Long.class))).thenReturn(mockAuction);
        when(orderService.findOrdersByUser(mockUser)).thenReturn(mockOrders);

        mockMvc.perform(post("/orderManagement/pay")
                        .param("auctionID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Il tuo ordine è stato inserito!"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", mockUser))
                .andExpect(request().attribute("orders", mockOrders))
                .andExpect(request().attribute("menuActiveLink", "Ordini"));

        // Verify that the auction and order services were called
        verify(auctionService).updateAuction(mockAuction);
        verify(orderService).createOrder(
                any(Timestamp.class),
                eq(mockProduct.getCurrent_price()),
                eq('N'),
                eq(mockUser),
                eq(mockProduct)
        );
    }

    @Test
    public void buyPremium_test() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(1L);
        mockUser.setRole("User");  // Utente non premium inizialmente

        Product mockPremiumProduct = new Product();
        mockPremiumProduct.setProductID(1L);
        mockPremiumProduct.setCurrent_price(50f);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp mockTimestamp = Timestamp.valueOf(currentDateTime);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(mockUser);
        when(productService.findProductById(1L)).thenReturn(mockPremiumProduct);

        mockMvc.perform(post("/orderManagement/premium"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderManagement/premium"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", mockUser))
                .andExpect(request().attribute("menuActiveLink", "Ordini"));

        // Verify that the user role was updated and user was saved
        verify(userService).updateUser(mockUser);
        assert "Premium".equals(mockUser.getRole());  // Verifica che il ruolo sia stato effettivamente aggiornato

        // Verify that the login cookie was created
        verify(userService).createLoginCookie(any(User.class), any(HttpServletResponse.class));

        // Verify that the order was created
        verify(orderService).createOrder(
                argThat(argument -> {
                    return Math.abs(argument.getTime() - mockTimestamp.getTime()) < 1000; // tolleranza di 1 secondo
                }),
                eq(mockPremiumProduct.getCurrent_price()),
                eq('N'),
                eq(mockUser),
                eq(mockPremiumProduct)
        );
    }

    @Test
    public void buyPremiumView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        Auction auction = new Auction();
        auction.setAuctionID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(eq(1L))).thenReturn(auction);

        mockMvc.perform(get("/orderManagement/premium"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderManagement/insertView"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("auction", auction))
                .andExpect(request().attribute("isPremium", true))
                .andExpect(request().attribute("menuActiveLink", "Ordini"));
    }

}