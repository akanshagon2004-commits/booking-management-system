package com.example.BookingManagement.repository;

import com.example.BookingManagement.model.Booking;
import com.example.BookingManagement.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Finds bookings specifically for the logged-in customer
    List<Booking> findByCustomerName(String customerName);

    long countByStatus(BookingStatus status);

    List<Booking> findTop5ByOrderByBookingDateDesc();
}