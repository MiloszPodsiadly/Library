package com.kodilla.library.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException (Long idUser,Long idBook) {
        super("no favorite titles");
    }
}
