package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ThresholdManagementController.class)
@AutoConfigureMockMvc
public class ThresholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ThresholdService thresholdService;

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
    public void testCheckOnUpdate_1iter(String pageToReturn, String auctionID, String price) {
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
    public void testCheckOnUpdate_2iter(String pageToReturn, String auctionID, String price) {
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

    @ParameterizedTest
    @CsvSource({
            "auctionManagement/view, 1, 150.0",
            "auctionManagement/lowerAllView, , "
    })
    public void testCheckOnUpdate_exception(String pageToReturn, String auctionID, String price) throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setCurrent_price(100.0f);
        product.setMin_price(80.0f);

        Auction auction = new Auction();
        auction.setAuctionID(1L);
        auction.setProduct_auctioned(product);

        List<Auction> auctions = new ArrayList<>();
        auctions.add(auction);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);
        when(auctionService.findAuctionById(any(Long.class))).thenReturn(auction);

        doThrow(new RuntimeException()).when(auctionService).updateAuction(any(Auction.class));

        if(pageToReturn.equals("auctionManagement/view")){
            mockMvc.perform(post("/thresholdManagement/update")
                            .param("auctionID", auctionID)
                            .param("pageToReturn",pageToReturn)
                            .param("price", price))
                    .andExpect(status().isOk())
                    .andExpect(view().name(pageToReturn))
                    .andExpect(request().attribute("applicationMessage", "Could not lower price!"));
        }

        if(pageToReturn.equals("auctionManagement/lowerAllView")){
            mockMvc.perform(post("/thresholdManagement/update")
                            .param("auctionID", auctionID)
                            .param("pageToReturn",pageToReturn)
                            .param("price", price))
                    .andExpect(status().isOk())
                    .andExpect(view().name(pageToReturn))
                    .andExpect(request().attribute("applicationMessage", "Could not lower prices!"));
        }
    }

    @Test
    public void testCheckPrices_NoThresholds() {
        ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

        Auction auction = new Auction();
        Product product = new Product();
        product.setCurrent_price(100.0f);
        product.setMin_price(80.0f);
        auction.setProduct_auctioned(product);

        // Simulazione di nessuna soglia trovata
        when(thresholdService.findThresholdsByAuction(auction)).thenReturn(Collections.emptyList());

        controller.checkPrices(auction);

        // Il metodo createOrderFromThreshold non è stato chiamato
        verify(controller, never()).createOrderFromThreshold(any(Auction.class), anyList());
    }

    @Test
    public void testCheckPrices_NoValidThresholds() {
        ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

        Auction auction = new Auction();
        Product product = new Product();
        product.setCurrent_price(100.0f);
        product.setMin_price(80.0f);
        auction.setProduct_auctioned(product);

        // Simulazione di soglie trovate ma nessuna valida
        Threshold threshold1 = new Threshold();
        threshold1.setPrice(50.0f);

        Threshold threshold2 = new Threshold();
        threshold2.setPrice(80.0f);

        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(threshold1);
        thresholds.add(threshold2);

        when(thresholdService.findThresholdsByAuction(auction)).thenReturn(thresholds);

        controller.checkPrices(auction);

        // Comunque mai chiamato
        verify(controller, never()).createOrderFromThreshold(any(Auction.class), anyList());
    }

    @Test
    public void testCheckPrices_ValidThresholds() {
        ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

        Auction auction = new Auction();
        Product product = new Product();
        product.setCurrent_price(100.0f);
        product.setMin_price(80.0f);
        auction.setProduct_auctioned(product);

        // Soglie trovate e valide
        Threshold threshold1 = new Threshold();
        threshold1.setPrice(120.0f);
        threshold1.setReservationDate(new Timestamp(System.currentTimeMillis()));

        Threshold threshold2 = new Threshold();
        threshold2.setPrice(100.0f);
        threshold1.setReservationDate(new Timestamp(System.currentTimeMillis()));

        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(threshold1);
        thresholds.add(threshold2);

        when(thresholdService.findThresholdsByAuction(auction)).thenReturn(thresholds);

        controller.checkPrices(auction);

        // Creazione di ArgumentCaptor specifico per List<Threshold>
        ArgumentCaptor<List<Threshold>> validThresholdsCaptor = ArgumentCaptor.forClass((Class<List<Threshold>>) (Class<?>) List.class);
        verify(controller).createOrderFromThreshold(eq(auction), validThresholdsCaptor.capture());

        // La lista catturata contiene solo le soglie valide
        List<Threshold> validThresholds = validThresholdsCaptor.getValue();
        assertEquals(2, validThresholds.size()); // Due soglie valide
    }

    @Test
    public void createOrderFromThreshold() {
        ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

        Product product = new Product();
        product.setProductID(1L);
        product.setCurrent_price(90.0f);

        Auction auction = new Auction();
        auction.setAuctionID(1L);
        auction.setProduct_auctioned(product);
        auction.setProduct_sold('N');

        List<Threshold> validThresholds = new ArrayList<>();

        User user = new User();
        user.setUserID(1L);

        Threshold threshold1 = new Threshold();
        threshold1.setPrice(80.0f);
        threshold1.setReservationDate(Timestamp.valueOf("2024-01-01 10:10:10"));
        threshold1.setUser(user);

        // Dovrei ordinare questa: prezzo più alto messa prima
        Threshold threshold2 = new Threshold();
        threshold2.setPrice(100.0f);
        threshold2.setReservationDate(Timestamp.valueOf("2024-01-01 10:10:10"));
        threshold2.setUser(user);

        Threshold threshold3 = new Threshold();
        threshold3.setPrice(100.0f);
        threshold3.setReservationDate(Timestamp.valueOf("2024-03-03 10:10:10"));
        threshold3.setUser(user);

        validThresholds.add(threshold1);
        validThresholds.add(threshold3); // Copertura condizioni!
        validThresholds.add(threshold2);

        controller.createOrderFromThreshold(auction, validThresholds);

        verify(thresholdService, times(3)).deleteThreshold(any(Threshold.class));
        verify(auctionService).updateAuction(auction);
        verify(orderService).createOrder(any(Timestamp.class),anyFloat(),anyChar(),any(User.class),any(Product.class));
        assertEquals(auction.getProduct_sold(), 'Y');
        // Verifico che il prezzo sia cambiato a causa della prenotazione
        assertEquals(auction.getProduct_auctioned().getCurrent_price(), threshold2.getPrice());
    }

    @Test
    public void createOrderFromThreshold_exceptions() {
        ThresholdManagementController controller = spy(new ThresholdManagementController(thresholdService, userService, auctionService, orderService, categoryService));

        Product product = new Product();
        product.setProductID(1L);
        product.setCurrent_price(90.0f);

        Auction auction = new Auction();
        auction.setAuctionID(1L);
        auction.setProduct_auctioned(product);
        auction.setProduct_sold('N');

        List<Threshold> validThresholds = new ArrayList<>();

        User user = new User();
        user.setUserID(1L);

        Threshold threshold1 = new Threshold();
        threshold1.setPrice(80.0f);
        threshold1.setReservationDate(Timestamp.valueOf("2024-01-01 10:10:10"));
        threshold1.setUser(user);

        // Dovrei ordinare questa: prezzo più alto messa prima
        Threshold threshold2 = new Threshold();
        threshold2.setPrice(100.0f);
        threshold2.setReservationDate(Timestamp.valueOf("2024-01-01 10:10:10"));
        threshold2.setUser(user);

        Threshold threshold3 = new Threshold();
        threshold3.setPrice(100.0f);
        threshold3.setReservationDate(Timestamp.valueOf("2024-03-03 10:10:10"));
        threshold3.setUser(user);

        validThresholds.add(threshold1);
        validThresholds.add(threshold2);
        validThresholds.add(threshold3);

        // Redirect System.err to capture the output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        doThrow(new RuntimeException()).when(thresholdService).deleteThreshold(any(Threshold.class));

        controller.createOrderFromThreshold(auction, validThresholds);

        System.setErr(originalErr);
        assertTrue(errContent.toString().contains("Could not delete threshold: " + threshold1));

        verify(thresholdService, times(3)).deleteThreshold(any(Threshold.class));
        verify(auctionService).updateAuction(auction);
        verify(orderService).createOrder(any(Timestamp.class),anyFloat(),anyChar(),any(User.class),any(Product.class));
        assertEquals(auction.getProduct_sold(), 'Y');
        assertEquals(auction.getProduct_auctioned().getCurrent_price(), threshold2.getPrice());

        reset(thresholdService);
        reset(orderService);
        reset(auctionService);

        doThrow(new RuntimeException()).when(orderService).createOrder(any(Timestamp.class),anyFloat(),anyChar(),any(User.class),any(Product.class));

        errContent = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        controller.createOrderFromThreshold(auction, validThresholds);

        System.setErr(originalErr);
        assertTrue(errContent.toString().contains("Could not create order from: " + threshold2));

        verify(thresholdService, times(0)).deleteThreshold(any(Threshold.class));
        verify(auctionService).updateAuction(auction);
        verify(orderService).createOrder(any(Timestamp.class),anyFloat(),anyChar(),any(User.class),any(Product.class));
        assertEquals(auction.getProduct_sold(), 'Y');
        assertEquals(auction.getProduct_auctioned().getCurrent_price(), threshold2.getPrice());

        reset(thresholdService);
        reset(orderService);
        reset(auctionService);

        doThrow(new RuntimeException()).when(auctionService).updateAuction(auction);

        errContent = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        controller.createOrderFromThreshold(auction, validThresholds);

        System.setErr(originalErr);
        assertTrue(errContent.toString().contains("Could not update auction: " + auction));

        verify(thresholdService, times(0)).deleteThreshold(any(Threshold.class));
        verify(auctionService).updateAuction(auction);
        verify(orderService, times(0)).createOrder(any(Timestamp.class),anyFloat(),anyChar(),any(User.class),any(Product.class));
        assertEquals(auction.getProduct_sold(), 'N');
        assertEquals(auction.getProduct_auctioned().getCurrent_price(), product.getCurrent_price());
    }

}