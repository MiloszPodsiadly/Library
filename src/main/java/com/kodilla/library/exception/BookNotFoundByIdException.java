package com.kodilla.library.exception;

public class BookNotFoundByIdException extends Exception {
    public BookNotFoundByIdException(Long idBook) {
        super(""+ idBook);
    }
}
