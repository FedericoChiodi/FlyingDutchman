package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ThresholdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuctionService auctionService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ThresholdService thresholdService;

    @InjectMocks
    private ThresholdManagementController thresholdManagementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(thresholdManagementController).build();
    }

    @Test
    public void view_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<Threshold> thresholds = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(thresholdService.findThresholdsByUser(any(User.class))).thenReturn(thresholds);

        mockMvc.perform(get("/thresholdManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("thresholds", thresholds))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        verify(userService, times(1)).findLoggedUser(any(HttpServletRequest.class));
        verify(thresholdService, times(1)).findThresholdsByUser(any(User.class));
    }

    @Test
    public void delete_test_SuccessAndFailure() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Threshold threshold = new Threshold();
        threshold.setThresholdID(1L);

        List<Threshold> thresholds = Collections.singletonList(threshold);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(thresholdService.findThresholdsByUser(loggedUser)).thenReturn(thresholds);
        when(thresholdService.findThresholdById(1L)).thenReturn(threshold);

        // Case 1: Successful deletion
        doNothing().when(thresholdService).deleteThreshold(threshold);

        mockMvc.perform(post("/thresholdManagement/delete")
                        .param("thresholdID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Eliminata correttamente!"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("thresholds", thresholds))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        // Case 2: Failed deletion
        doThrow(new RuntimeException()).when(thresholdService).deleteThreshold(threshold);

        mockMvc.perform(post("/thresholdManagement/delete")
                        .param("thresholdID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Could not delete threshold!"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("thresholds", thresholds))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));
    }

    @Test
    public void insert_test_SuccessAndFailure() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        List<Threshold> thresholds = Collections.singletonList(new Threshold());

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(1L)).thenReturn(auction);
        when(thresholdService.findThresholdsByUser(loggedUser)).thenReturn(thresholds);

        mockMvc.perform(post("/thresholdManagement/insert")
                        .param("auctionID", "1")
                        .param("price", "100.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("thresholds", thresholds))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        verify(thresholdService, times(1)).createThreshold(
                eq(100.0f),
                any(Timestamp.class),
                eq(loggedUser),
                eq(auction)
        );

        // Simulate failure during threshold creation
        doThrow(new RuntimeException("Test Exception")).when(thresholdService).createThreshold(
                anyFloat(), any(Timestamp.class), any(User.class), any(Auction.class)
        );

        mockMvc.perform(post("/thresholdManagement/insert")
                        .param("auctionID", "1")
                        .param("price", "100.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("thresholds", thresholds))
                .andExpect(request().attribute("menuActiveLink", "Prenota"))
                .andExpect(request().attribute("applicationMessage", "Could not insert threshold!"));

        verify(thresholdService, times(2)).createThreshold(
                eq(100.0f),
                any(Timestamp.class),
                eq(loggedUser),
                eq(auction)
        );
    }

    @Test
    public void insertView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(1L)).thenReturn(auction);

        mockMvc.perform(get("/thresholdManagement/insert")
                        .param("auctionID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/insModView"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("auction", auction))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        verify(userService, times(1)).findLoggedUser(any(HttpServletRequest.class));
        verify(auctionService, times(1)).findAuctionById(1L);
    }

    @Test
    public void modify_test_SuccessAndFailure() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Threshold threshold = new Threshold();
        threshold.setThresholdID(1L);
        threshold.setPrice(100.0f);
        threshold.setReservationDate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(threshold);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(thresholdService.findThresholdById(1L)).thenReturn(threshold);
        when(thresholdService.findThresholdsByUser(loggedUser)).thenReturn(thresholds);

        // Success case
        mockMvc.perform(post("/thresholdManagement/modify")
                        .param("thresholdID", "1")
                        .param("price", "150.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        ArgumentCaptor<Threshold> thresholdArgumentCaptor = ArgumentCaptor.forClass(Threshold.class);
        verify(thresholdService, times(1)).updateThreshold(thresholdArgumentCaptor.capture());

        Threshold updatedThreshold = thresholdArgumentCaptor.getValue();
        assertEquals(1L, updatedThreshold.getThresholdID());
        assertEquals(150.0f, updatedThreshold.getPrice());

        verify(thresholdService, times(1)).findThresholdsByUser(loggedUser);

        reset(thresholdService);

        when(thresholdService.findThresholdById(1L)).thenReturn(threshold);
        doThrow(new RuntimeException("Update failed")).when(thresholdService).updateThreshold(any(Threshold.class));
        when(thresholdService.findThresholdsByUser(loggedUser)).thenReturn(thresholds);

        // Failure case
        mockMvc.perform(post("/thresholdManagement/modify")
                        .param("thresholdID", "1")
                        .param("price", "150.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/view"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("menuActiveLink", "Prenota"))
                .andExpect(request().attribute("applicationMessage", "Could not update threshold!"));

        verify(thresholdService, times(1)).findThresholdsByUser(loggedUser);
    }

    @Test
    public void modifyView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Threshold threshold = new Threshold();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(thresholdService.findThresholdById(any(Long.class))).thenReturn(threshold);

        mockMvc.perform(get("/thresholdManagement/modify")
                        .param("thresholdID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("thresholdManagement/insModView"))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("threshold", threshold))
                .andExpect(request().attribute("menuActiveLink", "Prenota"));

        verify(userService, times(1)).findLoggedUser(any(HttpServletRequest.class));
        verify(thresholdService, times(1)).findThresholdById(any(Long.class));
    }

    @ParameterizedTest
    @CsvSource({
            "auctionManagement/view, 1, 150.0"
    })
    public void testCheckOnUpdate_view(String pageToReturn, String auctionID, String price) throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        if ("auctionManagement/view".equals(pageToReturn)) {
            Auction auction = new Auction();
            auction.setAuctionID(Long.parseLong(auctionID));

            Product product = new Product();
            product.setCurrent_price(Float.valueOf(price));
            product.setMin_price(10f);
            auction.setProduct_auctioned(product);

            List<Auction> auctions = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            when(auctionService.findAuctionById(Long.valueOf(auctionID))).thenReturn(auction);
            when(auctionService.findOpenAuctionsByOwnerNotDeleted(any(User.class))).thenReturn(auctions);
            when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

            mockMvc.perform(post("/thresholdManagement/update")
                            .param("pageToReturn", pageToReturn)
                            .param("auctionID", auctionID)
                            .param("price", price))
                    .andExpect(status().isOk())
                    .andExpect(request().attribute("applicationMessage", "Prezzo abbassato correttamente!"))
                    .andExpect(request().attribute("loggedUser", loggedUser))
                    .andExpect(request().attribute("loggedOn", true))
                    .andExpect(request().attribute("menuActiveLink", "Prenota"))
                    .andExpect(request().attribute("canEdit", true))
                    .andExpect(request().attribute("auctions", auctions))
                    .andExpect(request().attribute("categories", categories))
                    .andExpect(view().name(pageToReturn));

            // Eq a vedere se sono avvenuti 1 volta
            verify(auctionService).updateAuction(any(Auction.class));
            verify(auctionService).findAuctionById(Long.valueOf(auctionID));

            ArgumentCaptor<Auction> auctionArgumentCaptor = ArgumentCaptor.forClass(Auction.class);
            verify(auctionService, times(1)).updateAuction(auctionArgumentCaptor.capture());

            Auction updatedAuction = auctionArgumentCaptor.getValue();
            assertEquals(auctionID, updatedAuction.getAuctionID().toString());
            assertEquals(Float.valueOf(price), updatedAuction.getProduct_auctioned().getCurrent_price());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "auctionManagement/lowerAllView, , "
    })
    public void testCheckOnUpdate_1iter(String pageToReturn, String auctionID, String price) throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        // Verifica con 1 iterazione del for e prezzo > min
        if ("auctionManagement/lowerAllView".equals(pageToReturn)) {
            List<Auction> auctions = new ArrayList<>();
            Auction auction1 = new Auction();
            auction1.setAuctionID(1L);

            Product product1 = new Product();
            product1.setCurrent_price(100.0f);
            product1.setMin_price(85.0f);

            auction1.setProduct_auctioned(product1);
            auctions.add(auction1);

            when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);

            ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("pageToReturn", pageToReturn);
            request.addParameter("auctionID", auctionID);
            request.addParameter("price", price);

            // Il prezzo non diventa minore del minimo
            doReturn(0.05f).when(controller).generateRandom();

            controller.checkOnUpdate(request);

            assertEquals(0.05f, controller.generateRandom(), 0.001);
            assertEquals(95.0f, auctions.get(0).getProduct_auctioned().getCurrent_price());

            verify(auctionService).findAllOpenAuctionsExceptUser(any(User.class));
            verify(auctionService, times(1)).updateAuction(any(Auction.class));
        }

        // Verifica con 1 iterazione del for e prezzo <= min
        if ("auctionManagement/lowerAllView".equals(pageToReturn)) {
            reset(auctionService);

            List<Auction> auctions = new ArrayList<>();
            Auction auction1 = new Auction();
            auction1.setAuctionID(1L);

            Product product1 = new Product();
            product1.setCurrent_price(100.0f);
            product1.setMin_price(85.0f);

            auction1.setProduct_auctioned(product1);
            auctions.add(auction1);

            when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);

            ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("pageToReturn", pageToReturn);
            request.addParameter("auctionID", auctionID);
            request.addParameter("price", price);

            // Il prezzo diventa minore del minimo --> sarebbe 80€
            doReturn(0.2f).when(controller).generateRandom();

            controller.checkOnUpdate(request);

            assertEquals(0.2f, controller.generateRandom(), 0.001);
            assertEquals(85.0f, auctions.get(0).getProduct_auctioned().getCurrent_price());

            verify(auctionService, times(1)).updateAuction(any(Auction.class));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "auctionManagement/lowerAllView, , "
    })
    public void testCheckOnUpdate_2iter(String pageToReturn, String auctionID, String price) throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        // Verifica con 2 iterazioni del for e prezzo > min
        if ("auctionManagement/lowerAllView".equals(pageToReturn)) {
            List<Auction> auctions = new ArrayList<>();
            Auction auction1 = new Auction();
            auction1.setAuctionID(1L);

            Auction auction2 = new Auction();
            auction2.setAuctionID(2L);

            Product product1 = new Product();
            product1.setCurrent_price(100.0f);
            product1.setMin_price(85.0f);

            Product product2 = new Product();
            product2.setCurrent_price(200.0f);
            product2.setMin_price(180.0f);

            auction1.setProduct_auctioned(product1);
            auction2.setProduct_auctioned(product2);
            auctions.add(auction1);
            auctions.add(auction2);

            when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);

            ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("pageToReturn", pageToReturn);
            request.addParameter("auctionID", auctionID);
            request.addParameter("price", price);

            doReturn(0.05f).when(controller).generateRandom();

            controller.checkOnUpdate(request);

            assertEquals(0.05f, controller.generateRandom(), 0.001);
            assertEquals(95.0f, auctions.get(0).getProduct_auctioned().getCurrent_price());
            assertEquals(190.0f, auctions.get(1).getProduct_auctioned().getCurrent_price());

            verify(auctionService).findAllOpenAuctionsExceptUser(any(User.class));
            verify(auctionService, times(2)).updateAuction(any(Auction.class));
        }

        // Verifica con 2 iterazioni del for e prezzo <= min
        if("auctionManagement/lowerAllView".equals(pageToReturn)){
            reset(auctionService);

            List<Auction> auctions = new ArrayList<>();
            Auction auction1 = new Auction();
            auction1.setAuctionID(1L);

            Auction auction2 = new Auction();
            auction2.setAuctionID(2L);

            Product product1 = new Product();
            product1.setCurrent_price(100.0f);
            product1.setMin_price(85.0f);

            Product product2 = new Product();
            product2.setCurrent_price(200.0f);
            product2.setMin_price(180.0f);

            auction1.setProduct_auctioned(product1);
            auction2.setProduct_auctioned(product2);
            auctions.add(auction1);
            auctions.add(auction2);

            when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);

            ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("pageToReturn", pageToReturn);
            request.addParameter("auctionID", auctionID);
            request.addParameter("price", price);

            // Il prezzo diventa minore del minimo
            doReturn(0.2f).when(controller).generateRandom();

            controller.checkOnUpdate(request);

            assertEquals(0.2f, controller.generateRandom(), 0.001);
            assertEquals(85.0f, auctions.get(0).getProduct_auctioned().getCurrent_price());
            assertEquals(180.0f, auctions.get(1).getProduct_auctioned().getCurrent_price());

            verify(auctionService).findAllOpenAuctionsExceptUser(any(User.class));
            verify(auctionService, times(2)).updateAuction(any(Auction.class));
        }
    }

    @Test
    public void testCheckOnUpdate_exception() throws Exception {

    }

}