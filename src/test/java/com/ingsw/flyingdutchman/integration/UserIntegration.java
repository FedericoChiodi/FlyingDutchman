package com.ingsw.flyingdutchman.integration;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void init_dir() throws IOException {
        Files.createDirectories(Paths.get("/home/sanpc/uploads/loggedUser"));
        Files.createDirectories(Paths.get("/home/sanpc/uploads/otherUser"));
    }

    @BeforeEach
    public void setup() {
        User loggedUser = new User();
        loggedUser.setUsername("loggedUser");
        loggedUser.setPassword("password");
        loggedUser.setFirstname("First");
        loggedUser.setSurname("Last");
        loggedUser.setBirthdate(Date.valueOf("1990-01-01"));
        loggedUser.setAddress("123 Street");
        loggedUser.setCivic_number((short) 1);
        loggedUser.setCap((short) 123);
        loggedUser.setCity("City");
        loggedUser.setState("State");
        loggedUser.setEmail("loggedUser@example.com");
        loggedUser.setCel_number("1234567890");
        loggedUser.setRole("USER");
        loggedUser.setDeleted('N');
        userRepository.save(loggedUser);

        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword("password");
        otherUser.setFirstname("First");
        otherUser.setSurname("Last");
        otherUser.setBirthdate(Date.valueOf("1991-01-01"));
        otherUser.setAddress("456 Street");
        otherUser.setCivic_number((short) 2);
        otherUser.setCap((short) 543);
        otherUser.setCity("Other City");
        otherUser.setState("Other State");
        otherUser.setEmail("otherUser@example.com");
        otherUser.setCel_number("0987654321");
        otherUser.setRole("USER");
        otherUser.setDeleted('N');
        userRepository.save(otherUser);
    }

    @AfterAll
    public static void teardown() {
        FileSystemUtils.deleteRecursively(new File("/home/sanpc/uploads/loggedUser"));
        FileSystemUtils.deleteRecursively(new File("/home/sanpc/uploads/otherUser"));
    }

    @Test
    public void view_test() throws Exception {
        mockMvc.perform(get("/userManagement/view")
                        .cookie(new Cookie("loggedUser","loggedUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"))
                .andExpect(request().attribute("loggedOn", true))
                .andExpect(request().attribute("loggedUser", userRepository.findByUsername("loggedUser")));
    }

    @Test
    public void delete_test() throws Exception {
        mockMvc.perform(post("/userManagement/delete")
                        .cookie(new Cookie("loggedUser","loggedUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Account correttamente eliminato. Arrivederci!"))
                .andExpect(request().attribute("loggedOn", false));

        User deletedUser = userRepository.findByUsername("loggedUser");
        assert deletedUser == null;
    }

    @Test
    public void ban_test() throws Exception {
        mockMvc.perform(post("/userManagement/ban")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("username", "otherUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Bannato: otherUser"));

        User bannedUser = userRepository.findByUsername("otherUser");
        assert bannedUser == null;
    }

    @Test
    public void register_test() throws Exception {
        mockMvc.perform(post("/userManagement/register")
                        .param("username", "newUser")
                        .param("password", "newPassword")
                        .param("firstname", "New")
                        .param("surname", "User")
                        .param("birthdate", "2000-01-01")
                        .param("address", "789 Street")
                        .param("civic_number", "3")
                        .param("cap", "123")
                        .param("city", "New City")
                        .param("state", "New State")
                        .param("email", "newUser@example.com")
                        .param("cel_number", "1112223333")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("applicationMessage", "Account creato correttamente!"));

        User newUser = userRepository.findByUsername("newUser");
        assert newUser != null;
        assert newUser.getUsername().equals("newUser");
    }

    @Test
    public void modify_test() throws Exception {
        mockMvc.perform(post("/userManagement/modify")
                        .cookie(new Cookie("loggedUser","loggedUser"))
                        .param("username", "UPDATED_USER")
                        .param("password", "UPDATED_PASSWORD")
                        .param("firstname", "New")
                        .param("surname", "User")
                        .param("birthdate", "2000-01-01")
                        .param("address", "789 Street")
                        .param("civic_number", "3")
                        .param("cap", "123")
                        .param("city", "New City")
                        .param("state", "New State")
                        .param("email", "newUser@example.com")
                        .param("cel_number", "1112223333")
                        .param("role", "USER")
                        .param("deleted","N"))
                .andExpect(status().isOk())
                .andExpect(view().name("userManagement/view"));

        User newUser = userRepository.findByUsername("UPDATED_USER");
        assert newUser != null;
        assert newUser.getPassword().equals("UPDATED_PASSWORD");
    }

}
