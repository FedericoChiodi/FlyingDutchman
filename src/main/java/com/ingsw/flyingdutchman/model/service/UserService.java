package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password, String firstname, String surname, Date birthdate, String address, Short civic_number, Short cap, String city, String state, String email, String cel_number, String role) {
        return userRepository.create(username, password, firstname, surname, birthdate, address, civic_number, cap, city, state, email, cel_number, role);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findByUserID(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findAllUsersExceptMeAndDeleted(Long userID) {
        return userRepository.findAllByDeletedFalseAndUserIDNot(userID);
    }

    public void loginUser(User user, HttpServletResponse response) {
        // Crea un valore del cookie con le informazioni dell'utente
        String userInfo = user.getUsername();

        // Codifica le informazioni per sicurezza
        String encodedUserInfo = Base64.getEncoder().encodeToString(userInfo.getBytes());

        // Crea il cookie
        Cookie userCookie = new Cookie("loggedUser", encodedUserInfo);
        userCookie.setHttpOnly(true); // Sicurezza
        userCookie.setSecure(true); // Solo su HTTPS
        userCookie.setPath("/"); // Disponibile per tutta l'applicazione

        // Aggiungi il cookie alla risposta
        response.addCookie(userCookie);
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Trova il cookie esistente e rimuovilo
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loggedUser".equals(cookie.getName())) {
                    // Imposta la scadenza del cookie nel passato
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    public User findLoggedUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loggedUser".equals(cookie.getName())) {
                    String encodedUserInfo = cookie.getValue();
                    String decodedUserInfo = new String(Base64.getDecoder().decode(encodedUserInfo));

                    return userRepository.findByUsername(decodedUserInfo);
                }
            }
        }
        return null; // Utente non trovato
    }

}
