package com.kodilla.library.exception;

public class BookNotFoundByIdException extends RuntimeException {
    public BookNotFoundByIdException(Long idBook) {
        super(""+ idBook);
    }
}
