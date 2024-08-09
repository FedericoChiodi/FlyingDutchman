package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeManagementController {

    private final UserService userService;

    @Autowired
    public HomeManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/view", "/homeManagement/view"})
    public String view(HttpServletRequest request) {
        User userToAuthenticate = userService.findLoggedUser(request);

        request.setAttribute("loggedUser", userToAuthenticate);
        request.setAttribute("loggedOn", userToAuthenticate != null);

        return "homeManagement/view";
    }

    @RequestMapping(value = "/login", params = {"username","password"})
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "username", defaultValue = "") String username,
                        @RequestParam(value = "password", defaultValue = "") String password
                        ) {
        User userToAuthenticate = userService.findByUsername(username);
        String applicationMessage;

        if (userToAuthenticate != null) {
            if (userToAuthenticate.getPassword().equals(password) && userToAuthenticate.isDeleted() == 'N') {
                userService.createLoginCookie(userToAuthenticate, response);
            } else {
                applicationMessage = "Invalid username or password";
                request.setAttribute("applicationMessage", applicationMessage);
            }
        }
        else{
            applicationMessage = "Could not find that User";
            request.setAttribute("applicationMessage", applicationMessage);
        }

        request.setAttribute("loggedUser", userToAuthenticate);
        request.setAttribute("loggedOn", userToAuthenticate != null);

        return "homeManagement/view";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteLoginCookie(response);

        request.setAttribute("loggedUser", null);
        request.setAttribute("loggedOn", false);

        return "homeManagement/view";
    }
}