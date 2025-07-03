package com.kodilla.library.dto;

import java.time.LocalDateTime;

public record ReservationDTO(
        Long idReservation,
        Long idUser,
        Long idBook,
        LocalDateTime reservationDate,
        Boolean active,
        Integer reservationOrder,
        Boolean unavailableNotificationSent
) {}
