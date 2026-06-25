package com.example.BookingManagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // This ensures roles are saved as 'ROLE_CUSTOMER' instead of numbers (0, 1, 2)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}