package com.ingsw.flyingdutchman.services;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UploadController uploadController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleFileUpload() throws IOException {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockMultipartFile mockFile = new MockMultipartFile(
                "image", "testimage.png", "image/png", "some-image".getBytes()
        );

        when(userService.findLoggedUser(request)).thenReturn(mockUser);
        when(redirectAttributes.addFlashAttribute(anyString(), anyString())).thenReturn(redirectAttributes);

        // Act
        String viewName = uploadController.handleFileUpload("description", mockFile, request, redirectAttributes);

        // Assert
        verify(userService, times(1)).findLoggedUser(request);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "File uploaded successfully!");

        String expectedFilePath = "/home/sanpc/Uploads/testuser/description.png";
        File file = new File(expectedFilePath);

        assertEquals("productManagement/view", viewName);
        assertTrue(file.exists());

        // Cleanup
        if (file.exists()) {
            if(!file.delete())
                System.err.println("Failed to delete file");
        }
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

        UploadController controllerSpy = spy(new UploadController(userService));

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

        UploadController controllerSpy = spy(new UploadController(userService));

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
    public void testGetUploadDirectory() {
        String username = "testUser";

        File expectedDirectory = new File("/home/sanpc/Uploads/" + username);
        File actualDirectory = uploadController.getUploadDirectory(username);

        assertEquals(expectedDirectory, actualDirectory);
    }

}