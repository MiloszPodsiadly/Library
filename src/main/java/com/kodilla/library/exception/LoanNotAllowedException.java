package com.kodilla.library.exception;

public class LoanNotAllowedException extends RuntimeException {
    public LoanNotAllowedException(String message) {
        super(message);
    }
}
