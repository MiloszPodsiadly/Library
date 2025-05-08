package com.kodilla.library.exception;

public class BookAlreadyReservedException extends RuntimeException {

    public BookAlreadyReservedException(String message) {
        super(message);
    }
}
