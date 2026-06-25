package com.example.BookingManagement.repository;

import com.example.BookingManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Returns an Optional to safely handle cases where the user doesn't exist
    Optional<User> findByUsername(String username);
}