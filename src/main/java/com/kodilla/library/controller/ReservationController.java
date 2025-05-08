package com.kodilla.library.controller;

import com.kodilla.library.dto.ReservationDto;
import com.kodilla.library.model.User;
import com.kodilla.library.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Tworzenie rezerwacji
    @PostMapping("/create")
    public ResponseEntity<ReservationDto> createReservation(@RequestParam Long userId, @RequestParam Long bookId) {
        User user = new User();  // Załóżmy, że użytkownik jest już znaleziony
        ReservationDto reservationDto = reservationService.createReservation(user, bookId); // Metoda zwraca ReservationDto
        return new ResponseEntity<>(reservationDto, HttpStatus.CREATED);
    }

    // Pobieranie rezerwacji dla książki
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDto>> getReservationsForBook(@PathVariable Long bookId) {
        List<ReservationDto> reservations = reservationService.getReservationsForBook(bookId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Pobieranie rezerwacji dla użytkownika
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> getUserReservations(@PathVariable Long userId) {
        List<ReservationDto> reservations = reservationService.getUserReservations(userId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    }