package com.kodilla.library.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException (Long idUser,Long idBook) {
        super("This user has this book in his/her favorites list");
    }
}
