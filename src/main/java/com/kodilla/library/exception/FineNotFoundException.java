package com.kodilla.library.exception;

public class FineNotFoundException extends RuntimeException {
    public FineNotFoundException(Long idFine) {
        super("" + idFine);
    }
}
