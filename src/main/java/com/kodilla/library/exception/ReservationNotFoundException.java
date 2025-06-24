package com.kodilla.library.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long idReservation) {
        super("" + idReservation);
    }
}
