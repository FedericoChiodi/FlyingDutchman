package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@WebMvcTest(UserManagementController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ThresholdService thresholdService;

    @Test
    public void view_test() throws Exception {
        User user = new User();
        user.setUserID(1L);

        mockMvc.perform(get("/userManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"))
                .andExpect(request().attribute("loggedOn", false))
                .andExpect(request().attribute("menuActiveLink", "Utente"));

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(user);

        mockMvc.perform(get("/userManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"))
                .andExpect(request().attribute("loggedUser", user))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("menuActiveLink", "Utente"));
    }

    @Test
    public void delete_test() throws Exception {
        User user = new User();
        user.setUserID(1L);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(user);

        mockMvc.perform(post("/userManagement/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", false))
                .andExpect(request().attribute("applicationMessage", "Account correttamente eliminato. Arrivederci!"));

    }

    @Test
    public void ban_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        User user = new User();
        user.setUserID(2L);
        user.setUsername("Test");

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(userService.findByUsername(any(String.class))).thenReturn(user);

        mockMvc.perform(post("/userManagement/ban")
                        .param("username", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("applicationMessage", "Bannato: " + user.getUsername()));

    }

    @Test
    public void banView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        List<User> users = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(userService.findAllUsersExceptMeAndDeleted(loggedUser)).thenReturn(users);

        mockMvc.perform(get("/userManagement/ban"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/banView"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("usernames", users))
                .andExpect(request().attribute("menuActiveLink", "Banna"));

    }

    @Test
    public void insert_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        User user = new User();
        user.setUserID(2L);
        user.setUsername("Test");
        user.setPassword("Test");
        user.setFirstname("name");
        user.setSurname("surname");
        user.setBirthdate(new Date(System.currentTimeMillis()));
        user.setAddress("address");
        user.setCivic_number((short) 1);
        user.setCap((short) 1);
        user.setCity("city");
        user.setState("state");
        user.setEmail("email@email.com");
        user.setCel_number("123");
        user.setRole("role");

        // Senza loggedUser
        mockMvc.perform(post("/userManagement/register")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", false))
                .andExpect(request().attribute("applicationMessage","Account creato correttamente!"))
                .andExpect(request().attribute("menuActiveLink", "Utente"));

        verify(userService).create(anyString(),anyString(),anyString(),anyString(),any(Date.class),anyString(),anyShort(),anyShort(),anyString(),anyString(),anyString(),anyString(),anyString());

        reset(userService);

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        // Con loggedUser
        mockMvc.perform(post("/userManagement/register")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("applicationMessage","Account creato correttamente!"))
                .andExpect(request().attribute("menuActiveLink", "Utente"));

        verify(userService).create(anyString(),anyString(),anyString(),anyString(),any(Date.class),anyString(),anyShort(),anyShort(),anyString(),anyString(),anyString(),anyString(),anyString());

        reset(userService);

        doThrow(new RuntimeException()).when(userService).create(anyString(),anyString(),anyString(),anyString(),any(Date.class),anyString(),anyShort(),anyShort(),anyString(),anyString(),anyString(),anyString(),anyString());

        mockMvc.perform(post("/userManagement/register")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("applicationMessage","Username già esistente!"));

        verify(userService).create(anyString(),anyString(),anyString(),anyString(),any(Date.class),anyString(),anyShort(),anyShort(),anyString(),anyString(),anyString(),anyString(),anyString());
    }

    @Test
    public void insertView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        mockMvc.perform(get("/userManagement/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/insModView"))
                .andExpect(request().attribute("loggedOn", false))
                .andExpect(request().attribute("menuActiveLink", "Utente"));


        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        mockMvc.perform(get("/userManagement/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/insModView"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("menuActiveLink", "Utente"));
    }

    @Test
    public void modify_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        User user = new User();
        user.setUserID(2L);
        user.setUsername("Test");
        user.setPassword("Test");
        user.setFirstname("name");
        user.setSurname("surname");
        user.setBirthdate(new Date(System.currentTimeMillis()));
        user.setAddress("address");
        user.setCivic_number((short) 1);
        user.setCap((short) 1);
        user.setCity("city");
        user.setState("state");
        user.setEmail("email@email.com");
        user.setCel_number("123");
        user.setRole("role");

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        mockMvc.perform(post("/userManagement/modify")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                        .param("deleted","N")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("menuActiveLink", "Utente"));


        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).updateUser(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();
        assertEquals(updatedUser.getUsername(), user.getUsername());
        assertEquals(updatedUser.getPassword(), user.getPassword());

        doThrow(new RuntimeException()).when(userService).updateUser(any(User.class));

        mockMvc.perform(post("/userManagement/modify")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                        .param("deleted","N")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Could not update User!"));

        reset(auctionService);

        Auction auction = new Auction();

        when(auctionService.findAuctionById(anyLong())).thenReturn(auction);

        mockMvc.perform(post("/userManagement/modify")
                        .param("username", user.getUsername())
                        .param("password", user.getPassword())
                        .param("firstname", user.getFirstname())
                        .param("surname", user.getSurname())
                        .param("birthdate", user.getBirthdate().toString())
                        .param("address", user.getAddress())
                        .param("civic_number", user.getCivic_number().toString())
                        .param("cap", user.getCap().toString())
                        .param("city", user.getCity())
                        .param("state", user.getState())
                        .param("email", user.getEmail())
                        .param("cel_number", user.getCel_number())
                        .param("role", user.getRole())
                        .param("deleted","N")
                        .param("auctionID", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("auctionManagement/inspectAuction"))
                .andExpect(request().attribute("auction", auction));

        verify(auctionService).findAuctionById(anyLong());
    }

    @Test
    public void modifyView_test() throws Exception {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        mockMvc.perform(get("/userManagement/modify")
                        .param("auctionID","123"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/insModView"))
                .andExpect(request().attribute("loggedOn", false))
                .andExpect(request().attribute("auctionID", "123"))
                .andExpect(request().attribute("menuActiveLink", "Utente"));


        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);

        mockMvc.perform(get("/userManagement/modify")
                        .param("auctionID","123"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/insModView"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", loggedUser))
                .andExpect(request().attribute("auctionID","123"))
                .andExpect(request().attribute("menuActiveLink", "Utente"));
    }

    @Test
    public void delete_user_test() {
        User user = new User();
        user.setUsername("username");

        List<Threshold> thresholds = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Auction> auctions = new ArrayList<>();

        // Mock the File class
        File mockDirectory = mock(File.class);
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);

        when(mockDirectory.exists()).thenReturn(true);
        when(mockDirectory.isDirectory()).thenReturn(true);
        when(mockDirectory.listFiles()).thenReturn(new File[]{mockFile1, mockFile2});
        when(mockFile1.delete()).thenReturn(true);
        when(mockFile2.delete()).thenReturn(true);
        when(mockDirectory.delete()).thenReturn(true);

        arrange_delete(thresholds, products, auctions);

        UserManagementController controller = spy(new UserManagementController(userService,auctionService,productService,thresholdService));

        when(thresholdService.findThresholdsByUser(any(User.class))).thenReturn(thresholds);
        when(productService.findProductsByOwner(any(User.class))).thenReturn(products);
        when(auctionService.findByOwner(any(User.class))).thenReturn(auctions);

        controller.delete_user(user, mockDirectory);

        verify(thresholdService, times(thresholds.size())).deleteThreshold(any(Threshold.class));
        verify(productService, times(products.size())).deleteProduct(any(Product.class));
        verify(auctionService, times(auctions.size())).deleteAuction(any(Auction.class));
        verify(userService).deleteUser(user);

        verify(mockDirectory).listFiles();
        verify(mockFile1).delete();
        verify(mockFile2).delete();
        verify(mockDirectory).delete();
    }

    @Test
    public void delete_user_test_Exceptions() {
        User user = new User();
        user.setUsername("username");

        List<Threshold> thresholds = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Auction> auctions = new ArrayList<>();

        File mockDirectory = mock(File.class);
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);

        arrange_delete(thresholds,products,auctions);

        when(mockDirectory.listFiles()).thenReturn(new File[]{mockFile1, mockFile2});
        when(mockFile1.delete()).thenReturn(false);
        when(mockFile2.delete()).thenReturn(true);
        when(mockDirectory.delete()).thenReturn(false);

        UserManagementController controller = spy(new UserManagementController(userService,auctionService,productService,thresholdService));

        when(thresholdService.findThresholdsByUser(any(User.class))).thenReturn(thresholds);
        when(productService.findProductsByOwner(any(User.class))).thenReturn(products);
        when(auctionService.findByOwner(any(User.class))).thenReturn(auctions);

        doThrow(new RuntimeException()).when(thresholdService).deleteThreshold(any(Threshold.class));
        doThrow(new RuntimeException()).when(productService).deleteProduct(any(Product.class));
        doThrow(new RuntimeException()).when(auctionService).deleteAuction(any(Auction.class));
        doThrow(new RuntimeException()).when(userService).deleteUser(user);

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        controller.delete_user(user, mockDirectory);

        System.setErr(originalErr);
        assertTrue(errContent.toString().contains("Could not delete threshold: "));
        assertTrue(errContent.toString().contains("Could not delete product: "));
        assertTrue(errContent.toString().contains("Could not delete auction: "));
        assertTrue(errContent.toString().contains("Could not delete file: "));
        assertTrue(errContent.toString().contains("Could not delete directory: "));
        assertTrue(errContent.toString().contains("Could not delete user: "));
    }

    private void arrange_delete(List<Threshold> thresholds, List<Product> products, List<Auction> auctions) {
        Threshold threshold1 = new Threshold();
        Threshold threshold2 = new Threshold();
        Threshold threshold3 = new Threshold();
        thresholds.add(threshold1);
        thresholds.add(threshold2);
        thresholds.add(threshold3);

        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        Auction auction3 = new Auction();
        auctions.add(auction1);
        auctions.add(auction2);
        auctions.add(auction3);
    }
}