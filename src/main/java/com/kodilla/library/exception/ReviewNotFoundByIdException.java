package com.kodilla.library.exception;

public class ReviewNotFoundByIdException extends RuntimeException {
    public ReviewNotFoundByIdException(Long idReview) {
        super("" + idReview);
    }
}
