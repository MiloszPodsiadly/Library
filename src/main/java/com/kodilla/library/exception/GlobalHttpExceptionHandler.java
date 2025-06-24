package com.kodilla.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHttpExceptionHandler {

    @ExceptionHandler(BookNotFoundByIdException.class)
    public ResponseEntity<String> handleBookNotFound(BookNotFoundByIdException ex) {
        return new ResponseEntity<>("Book not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<String> handleFavoriteExists(FavoriteAlreadyExistsException ex) {
        return new ResponseEntity<>("Favorite already exists: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<String> handleFavoriteNotFound(FavoriteNotFoundException ex) {
        return new ResponseEntity<>("Favorite not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FineNotFoundException.class)
    public ResponseEntity<String> handleFineNotFound(FineNotFoundException ex) {
        return new ResponseEntity<>("Fine not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanNotAllowedException.class)
    public ResponseEntity<String> handleLoanNotAllowed(LoanNotAllowedException ex) {
        return new ResponseEntity<>("Loan not allowed: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LoanNotFoundByIdException.class)
    public ResponseEntity<String> handleLoanNotFound(LoanNotFoundByIdException ex) {
        return new ResponseEntity<>("Loan not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotAllowedException.class)
    public ResponseEntity<String> handleReservationNotAllowed(ReservationNotAllowedException ex) {
        return new ResponseEntity<>("Reservation not allowed: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFound(ReservationNotFoundException ex) {
        return new ResponseEntity<>("Reservation not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewNotFoundByIdException.class)
    public ResponseEntity<String> handleReviewNotFound(ReviewNotFoundByIdException ex) {
        return new ResponseEntity<>("Review not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundByIdException ex) {
        return new ResponseEntity<>("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundByMailException.class)
    public ResponseEntity<String> handleUserByEmailNotFound(UserNotFoundByMailException ex) {
        return new ResponseEntity<>("User not found by email: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurity(SecurityException ex) {
        return new ResponseEntity<>("Security violation: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>("Invalid request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
