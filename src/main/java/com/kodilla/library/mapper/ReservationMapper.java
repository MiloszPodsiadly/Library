package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.ReservationDTO;
import com.kodilla.library.model.Reservation;

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
                reservation.getReservationOrder(),
                reservation.getUnavailableNotificationSent()
        );
    }

    public List<ReservationDTO> toDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

