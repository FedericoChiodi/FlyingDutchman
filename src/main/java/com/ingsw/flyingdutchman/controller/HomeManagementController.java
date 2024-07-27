package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeManagementController {

    private final UserService userService;

    @Autowired
    public HomeManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/view"})
    public String view(HttpServletRequest request) {
        String username = request.getParameter("username");
        User userToAuthenticate = userService.findByUsername(username);

        request.setAttribute("loggedUser", userToAuthenticate);
        request.setAttribute("loggedOn", userToAuthenticate != null);

        return "homeManagement/view";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User userToAuthenticate = userService.findByUsername(username);
        String applicationMessage = null;

        if (userToAuthenticate != null) {
            if (userToAuthenticate.getPassword().equals(password) && userToAuthenticate.isDeleted() == 'N') {
                userService.createLoginCookie(userToAuthenticate, response);
            } else {
                applicationMessage = "Invalid username or password";
            }
        } else {
            applicationMessage = "Could not find such User";
        }

        request.setAttribute("loggedUser", userToAuthenticate);
        request.setAttribute("loggedOn", userToAuthenticate != null);
        request.setAttribute("applicationMessage", applicationMessage);

        return "homeManagement/view";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteLoginCookie(request, response);

        String applicationMessage = "Logged out successfully!";

        request.setAttribute("loggedUser", null);
        request.setAttribute("loggedOn", false);
        request.setAttribute("applicationMessage", applicationMessage);

        return "homeManagement/view";
    }
}