package com.example.BookingManagement.service;

import com.example.BookingManagement.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User saveUser(User user);
    User findByUsername(String username);
}