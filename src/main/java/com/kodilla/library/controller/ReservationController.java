package com.kodilla.library.controller;

import com.kodilla.library.dto.ReservationDTO;
import com.kodilla.library.exception.*;
import com.kodilla.library.mapper.ReservationMapper;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    // ✅ POST /reservations?userId=...&bookId=...
    @PostMapping
    public ResponseEntity<ReservationDTO> reserveBook(
            @RequestParam Long userId,
            @RequestParam Long bookId
    ) throws BookNotFoundByIdException, UserNotFoundByIdException {
        Reservation reservation = reservationService.reserveBook(userId, bookId);
        return ResponseEntity.ok(reservationMapper.toDto(reservation));
    }

    // ✅ DELETE /reservations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id)
            throws ReservationNotFoundException, ReservationNotAllowedException {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET /reservations/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(@PathVariable Long userId)
            throws UserNotFoundByIdException {
        List<Reservation> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservationMapper.toDtoList(reservations));
    }

    // ✅ GET /reservations/book/{bookId}
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> getBookReservations(@PathVariable Long bookId)
            throws BookNotFoundByIdException {
        List<Reservation> reservations = reservationService.getReservationsForBook(bookId);
        return ResponseEntity.ok(reservationMapper.toDtoList(reservations));
    }
}
