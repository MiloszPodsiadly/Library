package com.kodilla.library.exception;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(Long idUser,Long idBook) {
        super("This user doesn't have this book in his/her favorites list");
    }
}
