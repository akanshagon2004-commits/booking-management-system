package com.example.BookingManagement.exception;
public class InvalidBookingStateException extends RuntimeException {
    public InvalidBookingStateException(String message) { super(message); }
}