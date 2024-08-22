package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import com.ingsw.flyingdutchman.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        String username = "testUser";
        String password = "password";
        String firstname = "John";
        String surname = "Doe";
        Date birthdate = new Date(System.currentTimeMillis());
        String address = "123 Main St";
        Short civicNumber = 123;
        Short cap = 12345;
        String city = "TestCity";
        String state = "TestState";
        String email = "test@example.com";
        String celNumber = "1234567890";
        String role = "USER";

        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(username, password, firstname, surname, birthdate, address, civicNumber, cap, city, state, email, celNumber, role);

        assertNotNull(createdUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();

        userService.deleteUser(user);

        assertEquals('Y', user.isDeleted());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        User foundUser = userService.findByUsername("testUser");

        assertNotNull(foundUser);
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    public void testFindAllUsersExceptMeAndDeleted() {
        User currentUser = new User();
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAllUsersExceptMeAndDeleted(currentUser)).thenReturn(users);

        List<User> result = userService.findAllUsersExceptMeAndDeleted(currentUser);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAllUsersExceptMeAndDeleted(currentUser);
    }

    @Test
    public void testCreateLoginCookie() {
        User user = new User();
        user.setUsername("testUser");

        userService.createLoginCookie(user, response);

        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    public void testDeleteLoginCookie() {
        userService.deleteLoginCookie(response);

        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    public void testFindLoggedUser() {
        Cookie[] cookies = {new Cookie("loggedUser", "testUser")};
        when(request.getCookies()).thenReturn(cookies);
        User user = new User();
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User foundUser = userService.findLoggedUser(request);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    public void testFindLoggedUserNoCookies() {
        when(request.getCookies()).thenReturn(null);

        User foundUser = userService.findLoggedUser(request);

        assertNull(foundUser);
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    public void testFindLoggedUserCookieNotFound() {
        Cookie[] cookies = {new Cookie("someOtherCookie", "someValue")};
        when(request.getCookies()).thenReturn(cookies);

        User foundUser = userService.findLoggedUser(request);

        assertNull(foundUser);
        verify(userRepository, never()).findByUsername(anyString());
    }
}
