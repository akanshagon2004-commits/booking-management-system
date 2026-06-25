package com.example.BookingManagement.service;

import com.example.BookingManagement.exception.BookingNotFoundException;
import com.example.BookingManagement.model.Booking;
import com.example.BookingManagement.model.BookingStatus;
import com.example.BookingManagement.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking ID " + id + " not found."));
    }

    @Override
    public Booking saveBooking(Booking booking) {
        // Auto-generate ID: BKG + random 4 digits
        booking.setBookingId("BKG-" + (1000 + new Random().nextInt(9000)));
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setBookingDate(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        Booking existing = getBookingById(booking.getId());
        existing.setCustomerName(booking.getCustomerName());
        existing.setStatus(booking.getStatus());
        return bookingRepository.save(existing);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Cannot delete: Booking ID " + id + " not found.");
        }
        bookingRepository.deleteById(id);
    }
}