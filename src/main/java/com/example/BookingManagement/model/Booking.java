package com.example.BookingManagement.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity @Table(name = "bookings") @Data
public class Booking {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String bookingId;
        private String customerName;
        private Double amount;
        @Enumerated(EnumType.STRING) private BookingStatus status;
        private LocalDateTime bookingDate;
}