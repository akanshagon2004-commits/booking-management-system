package com.example.BookingManagement.controller;

import com.example.BookingManagement.repository.BookingRepository;
import com.example.BookingManagement.model.BookingStatus;
import com.example.BookingManagement.service.UserService;
import com.example.BookingManagement.model.User;
import com.example.BookingManagement.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class PageController {

    private final BookingRepository bookingRepo;
    private final UserService userService;

    public PageController(BookingRepository bookingRepo, UserService userService) {
        this.bookingRepo = bookingRepo;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // 1. Security Check: Ensure the user is actually logged in
        if (principal == null) {
            return "redirect:/login";
        }

        // 2. Fetch user details from the database using the security principal
        User loggedInUser = userService.findByUsername(principal.getName());

        // 3. Safety Check: Handle edge cases where user might be missing from DB
        if (loggedInUser == null) {
            return "redirect:/login?error";
        }

        // 4. Global UI Attributes: Always show the name and role on the sidebar/header
        model.addAttribute("userRole", loggedInUser.getRole().name());
        model.addAttribute("username", loggedInUser.getUsername());

        // 5. ROLE-BASED LOGIC

        // If the user is a CUSTOMER: Show ONLY their specific bookings
        if (loggedInUser.getRole() == Role.ROLE_CUSTOMER) {
            model.addAttribute("recentBookings", bookingRepo.findByCustomerName(loggedInUser.getUsername()));
            return "dashboard"; // Loads dashboard.html with restricted data
        }

        // If ROLE_USER or ROLE_ADMIN: Show the full management stats and top 5 recent bookings
        model.addAttribute("total", bookingRepo.count());
        model.addAttribute("requested", bookingRepo.countByStatus(BookingStatus.REQUESTED));
        model.addAttribute("approved", bookingRepo.countByStatus(BookingStatus.APPROVED));
        model.addAttribute("completed", bookingRepo.countByStatus(BookingStatus.COMPLETED));
        model.addAttribute("recentBookings", bookingRepo.findTop5ByOrderByBookingDateDesc());

        return "dashboard"; // Loads dashboard.html with full admin data
    }
}