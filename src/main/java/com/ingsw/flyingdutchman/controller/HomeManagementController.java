package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/homeManagement")
public class HomeManagementController {

    private final UserService userService;

    @Autowired
    public HomeManagementController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/view")
    public String view(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        User userToAuthenticate = userService.findByUsername(username);

        model.addAttribute("loggedUser", userToAuthenticate);
        model.addAttribute("loggedOn", userToAuthenticate != null);

        return "homeManagement/view";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
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

        model.addAttribute("loggedUser", userToAuthenticate);
        model.addAttribute("loggedOn", userToAuthenticate != null);
        model.addAttribute("applicationMessage", applicationMessage);

        return "homeManagement/view";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        userService.deleteLoginCookie(request, response);

        String applicationMessage = "Logged out successfully!";

        model.addAttribute("loggedUser", null);
        model.addAttribute("loggedOn", false);
        model.addAttribute("applicationMessage", applicationMessage);

        return "homeManagement/view";
    }
}
