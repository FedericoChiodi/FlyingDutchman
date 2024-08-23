package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionManagementController.class)
@AutoConfigureMockMvc
public class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ThresholdService thresholdService;

    @Test
    public void view_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(get("/auctionManagement/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("loggedOn", true))
            .andExpect(request().attribute("loggedUser", loggedUser))
            .andExpect(request().attribute("auctions", auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void viewMyAuctions_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findOpenAuctionsByOwnerNotDeleted(any(User.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(get("/auctionManagement/myAuctions"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("loggedOn", true))
            .andExpect(request().attribute("loggedUser", loggedUser))
            .andExpect(request().attribute("auctions", auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void insertView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductID(1L);
        Product product2 = new Product();
        product2.setProductID(2L);
        products.add(product1);
        products.add(product2);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findByOwnerNotInAuctions(any(User.class))).thenReturn(products);

        mockMvc.perform(get("/auctionManagement/insert"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/insertView"))
            .andExpect(request().attribute("loggedOn", true))
            .andExpect(request().attribute("loggedUser", loggedUser))
            .andExpect(request().attribute("menuActiveLink", "Aste"))
            .andExpect(request().attribute("products", products));
    }

    @Test
    public void insert_test_success() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setProductID(1L);

        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductById(any(Long.class))).thenReturn(product);
        when(auctionService.findByProductOpenNotDeleted(any(Product.class))).thenReturn(auctions);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(new ArrayList<>());
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(post("/auctionManagement/insert")
                    .param("productID", "1")
                    .param("opening_timestamp", "2024-01-01 00:00:00"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("applicationMessage", "Asta creata correttamente!"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auctions",auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void insert_test_failure() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setProductID(1L);

        List<Auction> auctions = new ArrayList<>();
        // Viene ritornata almeno 1 asta che contiene quel prodotto
        auctions.add(new Auction());

        List<Auction> auctions1 = new ArrayList<>();

        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductById(any(Long.class))).thenReturn(product);
        when(auctionService.findByProductOpenNotDeleted(any(Product.class))).thenReturn(auctions);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions1);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(post("/auctionManagement/insert")
                    .param("productID", "1")
                    .param("opening_timestamp", "2024-01-01 00:00:00"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("applicationMessage", "Prodotto già all'asta!"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auctions",auctions1))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void insert_test_exception() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setProductID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductById(anyLong())).thenReturn(product);
        when(auctionService.findByProductOpenNotDeleted(any(Product.class))).thenReturn(new ArrayList<>());

        doThrow(new RuntimeException()).when(auctionService).createAuction(any(Timestamp.class), any(Product.class));

        mockMvc.perform(post("/auctionManagement/insert")
                        .param("productID", "1")
                        .param("opening_timestamp", "2023-08-15 10:00:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Auction creation failed"));
    }

    @Test
    public void inspectAuction_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(any(Long.class))).thenReturn(auction);

        mockMvc.perform(get("/auctionManagement/inspect")
                    .param("auctionID","1"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/inspectAuction"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auction",auction))
            .andExpect(request().attribute("menuActiveLink","Aste"));

        mockMvc.perform(get("/auctionManagement/buyProduct")
                    .param("auctionID","1"))
            .andExpect(status().isOk())
            .andExpect(view().name("orderManagement/insertView"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auction",auction))
            .andExpect(request().attribute("menuActiveLink","Aste"));

        mockMvc.perform(get("/auctionManagement/update")
                    .param("auctionID","1"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/modifyPriceView"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auction",auction))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void update_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        Product product = new Product();
        product.setProductID(1L);
        product.setCurrent_price(10f);
        auction.setProduct_auctioned(product);

        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(any(Long.class))).thenReturn(auction);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(post("/auctionManagement/update")
                .param("auctionID","1")
                .param("price","5"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auctions",auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"))
            .andExpect(request().attribute("canEdit",true));

        // Verifica che il prezzo venga effettivamente aggiornato e modificato
        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionService, times(1)).updateAuction(auctionCaptor.capture());

        Auction updatedAuction = auctionCaptor.getValue();
        assertEquals(5f, updatedAuction.getProduct_auctioned().getCurrent_price());
    }

    @Test
    public void update_test_exception() throws Exception{
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(anyLong())).thenReturn(auction);

        doThrow(new RuntimeException("Auction update failed")).when(auctionService).updateAuction(any(Auction.class));

        mockMvc.perform(post("/auctionManagement/update")
                        .param("auctionID", "1")
                        .param("price", "50"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Auction update failed"));
    }

    @Test
    public void lowerAllView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        mockMvc.perform(get("/auctionManagement/lowerAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/lowerAllView"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("menuActiveLink","Abbassa"));
    }

    @Test
    public void delete_test() throws Exception{
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        List<Threshold> thresholds = new ArrayList<>();
        // Aggiungo 2 prenotazioni e vedo se le elimino entrambe
        Threshold threshold1 = new Threshold();
        Threshold threshold2 = new Threshold();
        thresholds.add(threshold1);
        thresholds.add(threshold2);

        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(any(Long.class))).thenReturn(auction);
        when(thresholdService.findThresholdsByAuction(any(Auction.class))).thenReturn(thresholds);
        when(auctionService.findAllOpenAuctionsExceptUser(any(User.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(post("/auctionManagement/delete")
                .param("auctionID","1"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auctions",auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("canEdit",true))
            .andExpect(request().attribute("menuActiveLink","Aste"));

        verify(thresholdService, times(1)).deleteThreshold(threshold1);
        verify(thresholdService, times(1)).deleteThreshold(threshold2);

        verify(auctionService, times(1)).deleteAuction(auction);
    }

    @Test
    public void delete_test_exception_threshold() throws Exception{
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(new Threshold());

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(anyLong())).thenReturn(auction);
        when(thresholdService.findThresholdsByAuction(any(Auction.class))).thenReturn(thresholds);

        doThrow(new RuntimeException("Threshold deletion failed")).when(thresholdService).deleteThreshold(any(Threshold.class));

        mockMvc.perform(post("/auctionManagement/delete")
                        .param("auctionID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Threshold deletion failed"));
    }

    @Test
    public void delete_test_exception_auction() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Auction auction = new Auction();
        auction.setAuctionID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionById(anyLong())).thenReturn(auction);
        when(thresholdService.findThresholdsByAuction(any(Auction.class))).thenReturn(new ArrayList<>());

        doThrow(new RuntimeException("Auction deletion failed")).when(auctionService).deleteAuction(any(Auction.class));

        mockMvc.perform(post("/auctionManagement/delete")
                        .param("auctionID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Auction deletion failed"));
    }

    @Test
    public void search_test() throws Exception{
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(auctionService.findAuctionByProductDescription(any(String.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(get("/auctionManagement/search")
                .param("auctionName","whatever"))
            .andExpect(status().isOk())
            .andExpect(view().name("auctionManagement/view"))
            .andExpect(request().attribute("loggedOn",true))
            .andExpect(request().attribute("loggedUser",loggedUser))
            .andExpect(request().attribute("auctions",auctions))
            .andExpect(request().attribute("categories",categories))
            .andExpect(request().attribute("menuActiveLink","Aste"));
    }

    @Test
    public void searchCategory_test() throws Exception{
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Category category = new Category();
        category.setCategoryID(1L);

        List<Auction> auctions = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(categoryService.findCategoryById(any(Long.class))).thenReturn(category);
        when(auctionService.findAuctionsByCategory(any(Category.class))).thenReturn(auctions);
        when(categoryService.getAllCategoriesExceptPremium()).thenReturn(categories);

        mockMvc.perform(get("/auctionManagement/searchCategory")
                        .param("categoryID","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/view"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("auctions",auctions))
                .andExpect(request().attribute("categories",categories))
                .andExpect(request().attribute("menuActiveLink","Aste"));
    }
}