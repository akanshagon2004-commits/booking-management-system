package com.example.BookingManagement.controller;

import com.example.BookingManagement.model.Role;
import com.example.BookingManagement.model.User;
import com.example.BookingManagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Added missing import
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        // Essential for Thymeleaf th:object="${user}" to work
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // SECURITY: Forced role assignment to prevent Admin spoofing
        user.setRole(Role.ROLE_CUSTOMER);
        userService.saveUser(user);
        return "redirect:/login?success";
    }
}