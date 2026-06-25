package com.example.BookingManagement.service;

import com.example.BookingManagement.model.Booking;
import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    Booking saveBooking(Booking booking);
    Booking updateBooking(Booking booking);
    void deleteBooking(Long id);
}