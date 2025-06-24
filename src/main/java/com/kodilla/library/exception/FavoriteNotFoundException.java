package com.kodilla.library.exception;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(Long idUser,Long idBook) {
        super("no titles here");
    }
}
