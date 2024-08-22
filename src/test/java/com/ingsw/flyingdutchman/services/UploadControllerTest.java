package com.ingsw.flyingdutchman.services;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.CategoryService;
import com.ingsw.flyingdutchman.model.service.ProductService;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UploadController uploadController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(uploadController).build();
    }

    @AfterAll
    public static void teardown() {
        File directory = new File("/home/sanpc/uploads/testuser");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if(!file.delete())
                        System.out.println("Failed to delete file " + file.getName());
                }
            }
        }
        if(!directory.delete())
            System.out.println("Failed to delete directory");
    }

    @Test
    public void test_request_params() throws Exception {
        User loggedUser = new User();
        loggedUser.setUsername("testuser");

        List<Product> products = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "test image content".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/Upload")
                        .file(file)
                        .param("description", "testDescription"))
                .andExpect(status().isOk())
                .andReturn();

        HttpServletRequest request = result.getRequest();
        Boolean loggedOn = (Boolean) request.getAttribute("loggedOn");

        assertEquals(Boolean.TRUE, loggedOn);
        assertEquals(loggedUser, request.getAttribute("loggedUser"));
        assertEquals(products, request.getAttribute("products"));
        assertEquals(false, request.getAttribute("soldProductsAction"));
        assertEquals("Prodotti", request.getAttribute("menuActiveLink"));
    }

    @Test
    public void testHandleFileUpload() throws IOException {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        Category category = new Category();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "testimage.png", "image/png", "some-image".getBytes()
        );

        when(userService.findLoggedUser(request)).thenReturn(mockUser);
        when(categoryService.findCategoryById(anyLong())).thenReturn(category);
        when(redirectAttributes.addFlashAttribute(anyString(), anyString())).thenReturn(redirectAttributes);

        request.setParameter("min_price","100");
        request.setParameter("starting_price","120");
        request.setParameter("categoryID","1");

        // Act
        String viewName = uploadController.handleFileUpload("description", mockFile, request, redirectAttributes);

        // Assert
        verify(userService, times(1)).findLoggedUser(request);
        verify(categoryService, times(1)).findCategoryById(anyLong());
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "File uploaded successfully!");
        verify(productService, times(1)).createProduct(
                eq("description"),
                eq(100f),
                eq(120f),
                eq(120f),
                eq("/home/sanpc/uploads/testuser" + File.separator + "description.png"),
                eq(category),
                eq(mockUser)
        );

        String expectedFilePath = "/home/sanpc/uploads/testuser/description.png";
        File file = new File(expectedFilePath);

        assertEquals("productManagement/view", viewName);
        assertTrue(file.exists());
    }
    
    @Test
    public void testCreationException() throws Exception {
        User loggedUser = new User();
        loggedUser.setUsername("testuser");

        List<Product> products = new ArrayList<>();

        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(loggedUser);
        when(productService.findProductByOwnerNotDeletedNotSold(any(User.class))).thenReturn(products);
        doThrow(new RuntimeException()).when(productService).createProduct(anyString(),anyFloat(),anyFloat(),anyFloat(),anyString(),any(Category.class),any(User.class));

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "test image content".getBytes()
        );

        MvcResult result = mockMvc.perform(multipart("/Upload")
                        .file(file)
                        .param("description", "testDescription"))
                .andExpect(status().isOk())
                .andReturn();

        HttpServletRequest request = result.getRequest();
        assertEquals("Could not create product!", request.getAttribute("applicationMessage"));
    }

    @Test
    public void testHandleFileUpload_FolderCreationFail() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "testimage.jpg", "image/jpeg", "some-image".getBytes()
        );

        when(userService.findLoggedUser(request)).thenReturn(mockUser);

        UploadController controllerSpy = spy(new UploadController(userService, productService, categoryService));

        File mockDir = mock(File.class);
        when(controllerSpy.getUploadDirectory(mockUser.getUsername())).thenReturn(mockDir);
        when(mockDir.exists()).thenReturn(false);
        when(mockDir.mkdirs()).thenReturn(false); // Simula il fallimento della creazione

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            controllerSpy.handleFileUpload("description", mockFile, request, redirectAttributes);
        });

        assertEquals("Directory could not be created.", exception.getMessage());
        verify(controllerSpy).getUploadDirectory("testuser");
    }

    @Test
    public void testHandleFileUpload_FolderCreationSuccess() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "testimage.jpg", "image/jpeg", "some-image".getBytes()
        );

        when(userService.findLoggedUser(request)).thenReturn(mockUser);

        UploadController controllerSpy = spy(new UploadController(userService, productService, categoryService));

        File mockDir = mock(File.class);
        when(controllerSpy.getUploadDirectory(mockUser.getUsername())).thenReturn(mockDir);
        when(mockDir.exists()).thenReturn(false);
        when(mockDir.mkdirs()).thenReturn(true);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            controllerSpy.handleFileUpload("description", mockFile, request, redirectAttributes);
        });

        assertNotEquals("Directory could not be created.", exception.getMessage());
        verify(controllerSpy).getUploadDirectory("testuser");
    }

    @Test
    public void testHandleFileUpload_FolderPresent() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "testimage.jpg", "image/jpeg", "some-image".getBytes()
        );

        when(userService.findLoggedUser(request)).thenReturn(mockUser);

        UploadController controllerSpy = spy(new UploadController(userService, productService, categoryService));

        File mockDir = mock(File.class);
        when(controllerSpy.getUploadDirectory(mockUser.getUsername())).thenReturn(mockDir);
        when(mockDir.exists()).thenReturn(true);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            controllerSpy.handleFileUpload("description", mockFile, request, redirectAttributes);
        });

        assertNotEquals("Directory could not be created.", exception.getMessage());
        verify(controllerSpy).getUploadDirectory("testuser");
    }

    @Test
    public void testGetUploadDirectory() {
        String username = "testuser";

        File expectedDirectory = new File("/home/sanpc/uploads/" + username);
        File actualDirectory = uploadController.getUploadDirectory(username);

        assertEquals(expectedDirectory, actualDirectory);
    }

}