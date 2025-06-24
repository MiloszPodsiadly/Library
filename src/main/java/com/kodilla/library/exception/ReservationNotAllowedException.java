package com.kodilla.library.exception;

public class ReservationNotAllowedException extends RuntimeException {
    public ReservationNotAllowedException(String message) {
        super(message);
    }
}
