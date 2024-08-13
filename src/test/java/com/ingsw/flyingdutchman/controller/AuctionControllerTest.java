package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuctionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuctionService auctionService;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private ThresholdService thresholdService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuctionManagementController auctionManagementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auctionManagementController).build();
    }

    @AfterEach
    public void teardown(){

    }

    @Test
    public void testView() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        List<Auction> auctions = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAllOpenAuctionsExceptUser(loggedUser)).thenReturn(auctions);

        mockMvc.perform(get("/auctionManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("auctions", auctions));    }

    @Test
    public void testInsertAuctionSuccess() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        Product product = new Product();
        product.setProductID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByIdNotDeletedNotSoldNotInAuction(any(Long.class))).thenReturn(product);
        when(request.getParameter("opening_timestamp")).thenReturn(Timestamp.valueOf("2024-01-01 00:00:00").toString());

        mockMvc.perform(post("/auctionManagement/insert")
                        .param("productID", "1")
                        .param("opening_timestamp", "2024-01-01 00:00:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(model().attribute("applicationMessage", "Asta creata correttamente!"));
    }

    @Test
    public void testInsertAuctionFailure() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByIdNotDeletedNotSoldNotInAuction(any(Long.class))).thenReturn(null);

        mockMvc.perform(post("/auctionManagement/insert")
                        .param("productID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(model().attribute("applicationMessage", "Prodotto già all'asta!"));
    }
}
