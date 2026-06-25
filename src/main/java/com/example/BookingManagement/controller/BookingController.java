package com.example.BookingManagement.controller;

import com.example.BookingManagement.model.Booking;
import com.example.BookingManagement.model.BookingStatus;
import com.example.BookingManagement.model.Role;
import com.example.BookingManagement.model.User;
import com.example.BookingManagement.service.BookingService;
import com.example.BookingManagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // 1. List: Added userRole so the sidebar/table buttons show up
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String listBookings(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("userRole", user.getRole().name());
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    @GetMapping("/add")
    public String addForm(Model model, Principal principal) {
        model.addAttribute("booking", new Booking());
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("userRole", user.getRole().name());
        }
        return "add-booking";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Booking booking, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user.getRole() == Role.ROLE_CUSTOMER) {
            booking.setCustomerName(user.getUsername());
            booking.setAmount(1500.0);
            booking.setStatus(BookingStatus.REQUESTED);
        }
        if (booking.getBookingId() == null || booking.getBookingId().isEmpty()) {
            booking.setBookingId("BKG-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        }
        booking.setBookingDate(LocalDateTime.now());
        bookingService.saveBooking(booking);
        return (user.getRole() == Role.ROLE_CUSTOMER) ? "redirect:/dashboard" : "redirect:/bookings/list";
    }

    // 2. Edit: Added userRole so the "Back to List" and "Update" buttons work
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String editForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("userRole", user.getRole().name());
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "edit-booking";
    }

    // 3. Update: Added explicit permission check
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String update(@ModelAttribute Booking booking) {
        bookingService.updateBooking(booking);
        return "redirect:/bookings/list";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')") // Allows both to delete, or use hasAuthority('ROLE_ADMIN')
    public String delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings/list";
    }
}