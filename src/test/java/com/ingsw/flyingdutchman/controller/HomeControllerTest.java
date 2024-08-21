package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
public class HomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private HomeManagementController homeManagementController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeManagementController).build();
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    public void view_test(User userToAuthenticate, boolean expectedLoggedOn, User expectedUser) throws Exception {
        when(userService.findLoggedUser(any(HttpServletRequest.class))).thenReturn(userToAuthenticate);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", expectedLoggedOn))
                .andExpect(request().attribute("loggedUser", expectedUser));

        mockMvc.perform(get("/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", expectedLoggedOn))
                .andExpect(request().attribute("loggedUser", expectedUser));

        mockMvc.perform(get("/homeManagement/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", expectedLoggedOn))
                .andExpect(request().attribute("loggedUser", expectedUser));
    }

    @ParameterizedTest
    @MethodSource("loginProvider")
    public void loginTest(String username, String password, User userToAuthenticate, boolean expectedLoggedOn, User expectedLoggedUser, String expectedMessage) throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(userToAuthenticate);

        if (userToAuthenticate != null && userToAuthenticate.getPassword().equals(password) && userToAuthenticate.isDeleted() == 'N') {
            doNothing().when(userService).createLoginCookie(any(User.class), any(HttpServletResponse.class));
        }

        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", expectedLoggedOn))
                .andExpect(request().attribute("loggedUser", expectedLoggedUser));

        if (expectedMessage != null) {
            mockMvc.perform(post("/login")
                            .param("username", username)
                            .param("password", password))
                    .andExpect(request().attribute("applicationMessage", expectedMessage));
        }
    }

    @Test
    public void logoutTest() throws Exception {
        doNothing().when(userService).deleteLoginCookie(any(HttpServletResponse.class));

        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeManagement/view"))
                .andExpect(request().attribute("loggedOn", false));
    }

    private static @NotNull Stream<Object[]> userProvider() {
        User loggedUser = new User();
        loggedUser.setUserID(1L);

        return Stream.of(
                new Object[]{null, false, null},     // Caso utente nullo
                new Object[]{loggedUser, true, loggedUser}  // Caso utente presente
        );
    }

    private static @NotNull Stream<Object[]> loginProvider() {
        User validUser = new User();
        validUser.setUserID(1L);
        validUser.setUsername("validUser");
        validUser.setPassword("correctPassword");
        validUser.setDeleted('N');

        User deletedUser = new User();
        deletedUser.setUserID(2L);
        deletedUser.setUsername("deletedUser");
        deletedUser.setPassword("correctPassword");
        deletedUser.setDeleted('Y');

        return Stream.of(
                new Object[]{"nonExistentUser", "anyPassword", null, false, null, "Could not find that User"}, // Username non trovato
                new Object[]{"validUser", "wrongPassword", validUser, false, null, "Invalid username or password"}, // Password errata
                new Object[]{"deletedUser", "correctPassword", deletedUser, false, null, "Invalid username or password"}, // Utente cancellato
                new Object[]{"validUser", "correctPassword", validUser, true, validUser, null} // Login corretto
        );
    }
}