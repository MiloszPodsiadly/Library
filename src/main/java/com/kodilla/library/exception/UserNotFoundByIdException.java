package com.kodilla.library.exception;

public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(Long idUser) {
        super("" + idUser);
    }
}
