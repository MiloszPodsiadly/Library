package com.kodilla.library.exception;

public class UserNotFoundByMailException extends RuntimeException {
    public UserNotFoundByMailException(String message) {
        super(message);
    }
}
