package com.kodilla.library.mapper;

import com.kodilla.library.dto.ReservationDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public ReservationDTO toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationDTO(
                reservation.getIdReservation(),
                reservation.getUser().getIdUser(),
                reservation.getBook().getIdBook(),
                reservation.getReservationDate(),
                reservation.getActive(),
                reservation.getReservationOrder()
        );
    }

    public Reservation toEntity(ReservationDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }
        return Reservation.builder()
                .idReservation(dto.idReservation())
                .user(user)
                .book(book)
                .reservationDate(dto.reservationDate())
                .active(dto.active())
                .reservationOrder(dto.reservationOrder())
                .build();
    }

    public List<ReservationDTO> toDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

