package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodilla.library.dto.ReservationDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.ReservationNotAllowedException;
import com.kodilla.library.exception.ReservationNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.ReservationMapper;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<ReservationDTO> reserveBook(
            @RequestParam Long idUser,
            @RequestParam Long idBook
    ) throws BookNotFoundByIdException, UserNotFoundByIdException {
        Reservation reservation = reservationService.reserveBook(idUser, idBook);
        return ResponseEntity.ok(reservationMapper.toDto(reservation));
    }

    @DeleteMapping("/{idReservation}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long idReservation)
            throws ReservationNotFoundException, ReservationNotAllowedException {
        reservationService.cancelReservation(idReservation);
        return ResponseEntity.ok("Reservation cancelled");
    }

    @DeleteMapping("/delete/{idReservation}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long idReservation)
            throws ReservationNotFoundException, ReservationNotAllowedException {
        reservationService.deleteReservation(idReservation);
        return ResponseEntity.ok("Reservation deleted");
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        List<Reservation> reservations = reservationService.getReservationsByUser(idUser);
        return ResponseEntity.ok(reservationMapper.toDtoList(reservations));
    }

    @GetMapping("/book/{idBook}")
    public ResponseEntity<List<ReservationDTO>> getBookReservations(@PathVariable Long idBook)
            throws BookNotFoundByIdException {
        List<Reservation> reservations = reservationService.getReservationsForBook(idBook);
        return ResponseEntity.ok(reservationMapper.toDtoList(reservations));
    }
}
