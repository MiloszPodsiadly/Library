package com.kodilla.library.mapper;

import com.kodilla.library.dto.ReservationDTO;
import com.kodilla.library.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationMapperTest {

    private ReservationMapper mapper;
    private Reservation reservation;

    @BeforeEach
    void setup() {
        mapper = new ReservationMapper();

        User user = User.builder()
                .idUser(1L)
                .email("alice@example.com")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("Clean Code")
                .statuses(new HashSet<>())
                .build();

        reservation = Reservation.builder()
                .idReservation(3L)
                .user(user)
                .book(book)
                .reservationDate(LocalDateTime.of(2024, 4, 1, 10, 0))
                .reservationOrder(1)
                .active(true)
                .unavailableNotificationSent(false)
                .build();

        System.out.println("🔧 Initialized test data for ReservationMapper.");
    }

    @Test
    @DisplayName("📌 Should map Reservation to ReservationDTO")
    void shouldMapToDto() {
        ReservationDTO dto = mapper.toDto(reservation);

        assertThat(dto).isNotNull();
        assertThat(dto.idReservation()).isEqualTo(3L);
        assertThat(dto.idUser()).isEqualTo(1L);
        assertThat(dto.idBook()).isEqualTo(2L);
        assertThat(dto.active()).isTrue();
        assertThat(dto.reservationOrder()).isEqualTo(1);
        assertThat(dto.unavailableNotificationSent()).isFalse();

        System.out.println("✅ Successfully mapped Reservation to ReservationDTO.");
    }

    @Test
    @DisplayName("📚 Should map list of Reservations to list of DTOs")
    void shouldMapReservationListToDtoList() {
        List<ReservationDTO> result = mapper.toDtoList(List.of(reservation));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idReservation()).isEqualTo(3L);

        System.out.println("📋 Successfully mapped Reservation list to DTO list.");
    }
}
