package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

public class ProductControllerTest {

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
    private OrderService orderService;

    @InjectMocks
    private ProductManagementController productManagementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productManagementController).build();
    }

    @Test
    public void view_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<Product> products = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductById(any(Long.class))).thenReturn(new Product());

        mockMvc.perform(get("/productManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("products", products))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"));
    }

    @Test
    public void insertView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<Category> categories = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/productManagement/insertView"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/insertView"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("categories", categories))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"));
    }

    @Test
    public void insert_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        loggedUser.setUsername("testuser");

        Category category = new Category();
        category.setCategoryID(1L);

        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();


        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(categoryService.findCategoryById(anyLong())).thenReturn(category);
        when(categoryService.getAllCategories()).thenReturn(categories);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);

        mockMvc.perform(post("/productManagement/insert")
                        .param("description", "Test Product")
                        .param("min_price", "100")
                        .param("starting_price", "120")
                        .param("current_price", "150")
                        .param("categoryID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("products",products))
                .andExpect(request().attribute("soldProductsAction",false))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"));

        verify(productService, times(1)).createProduct(
                eq("Test Product"),
                eq(100f),
                eq(120f),
                eq(150f),
                eq("/srv/flyingdutchman/uploads/testuser" + File.separator + "Test Product.png"),
                eq(category),
                eq(loggedUser)
        );
    }

    @Test
    public void insert_test_exception() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);
        loggedUser.setUsername("testUser");

        Category category = new Category();
        category.setCategoryID(1L);

        List<Category> categories = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(categoryService.findCategoryById(anyLong())).thenReturn(category);
        when(categoryService.getAllCategories()).thenReturn(categories);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);
        when(productService.createProduct(any(String.class), any(Float.class), any(Float.class), any(Float.class), any(String.class), any(Category.class), any(User.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/productManagement/insert")
                        .param("description", "Test Product")
                        .param("min_price", "100")
                        .param("starting_price", "120")
                        .param("current_price", "150")
                        .param("categoryID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("products",products))
                .andExpect(request().attribute("soldProductsAction",false))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"))
                .andExpect(request().attribute("applicationMessage","Could not create product!"));
    }

    @Test
    public void delete_test_ShouldHandleAllScenarios() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setProductID(1L);

        List<Product> products = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductById(anyLong())).thenReturn(product);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);

        // Case 1: Prodotto in asta
        when(auctionService.findByProductOpenNotDeleted(product)).thenReturn(Collections.singletonList(new Auction()));

        mockMvc.perform(post("/productManagement/delete")
                        .param("productID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Non puoi eliminare un prodotto attualmente in asta!"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("products",products))
                .andExpect(request().attribute("soldProductsAction",false))
                .andExpect(request().attribute("menuActiveLink","Prodotti"));

        // Verifica che il prodotto non venga eliminato
        verify(productService, never()).deleteProduct(product);

        // Case 2: Prodotto non in asta, eliminazione riuscita
        when(auctionService.findByProductOpenNotDeleted(product)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/productManagement/delete")
                        .param("productID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("products",products))
                .andExpect(request().attribute("soldProductsAction",false))
                .andExpect(request().attribute("menuActiveLink","Prodotti"));

        // Verifica che il prodotto venga eliminato
        verify(productService, times(1)).deleteProduct(product);

        // Reset the mocks for the next case
        reset(productService);

        when(productService.findProductById(anyLong())).thenReturn(product);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);

        // Case 3: Prodotto non in asta, eliminazione fallita
        doThrow(new RuntimeException("Delete failed")).when(productService).deleteProduct(product);

        mockMvc.perform(post("/productManagement/delete")
                        .param("productID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Could not delete product!"))
                .andExpect(request().attribute("loggedOn",true))
                .andExpect(request().attribute("loggedUser",loggedUser))
                .andExpect(request().attribute("products",products))
                .andExpect(request().attribute("soldProductsAction",false))
                .andExpect(request().attribute("menuActiveLink","Prodotti"));

        // Verifica che il prodotto abbia tentato di essere eliminato
        verify(productService, times(1)).deleteProduct(product);
    }

    @Test
    public void viewSoldProducts_test_ShouldHandleVariousScenarios() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        Product product = new Product();
        product.setProductID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByOwnerNotDeletedSold(loggedUser)).thenReturn(Collections.singletonList(product));

        // Case 1: Utente loggato con prodotti venduti e ordini associati
        mockMvc.perform(get("/productManagement/viewSold"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("products", Collections.singletonList(product)))
                .andExpect(request().attribute("soldProductsAction", true))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"));

        // Verifica che i metodi dei servizi siano stati chiamati
        verify(userService, times(1)).findLoggedUser(any(HttpServletRequest.class));
        verify(productService, times(1)).findProductByOwnerNotDeletedSold(loggedUser);

        // Reset the mocks for the next case
        reset(userService, productService, orderService);

        // Case 2: Utente loggato senza prodotti venduti
        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByOwnerNotDeletedSold(loggedUser)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/productManagement/viewSold"))
                .andExpect(status().isOk())
                .andExpect(view().name("productManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("products", Collections.emptyList()))
                .andExpect(request().attribute("soldProductsAction", true))
                .andExpect(request().attribute("menuActiveLink", "Prodotti"));

        // Verifica che i metodi dei servizi siano stati chiamati correttamente
        verify(userService, times(1)).findLoggedUser(any(HttpServletRequest.class));
        verify(productService, times(1)).findProductByOwnerNotDeletedSold(loggedUser);
    }
}