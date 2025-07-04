package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @Test
    @DisplayName("📌 Should build reservation with default values")
    void shouldBuildWithDefaults() {
        User user = User.builder().idUser(1L).name("Alice").build();
        Book book = Book.builder().idBook(2L).title("Clean Code").build();
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .reservationDate(now)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(1).plusHours(6))
                .build();

        assertThat(reservation.getActive()).isTrue();
        assertThat(reservation.getReservationOrder()).isEqualTo(1);
        assertThat(reservation.getUnavailableNotificationSent()).isFalse();
        assertThat(reservation.getCreatedAt()).isNotNull();

        System.out.println("✅ Reservation created for book: " + reservation.getBook().getTitle());
        System.out.println("👤 User: " + reservation.getUser().getName());
        System.out.println("📅 From: " + reservation.getStartDate() + " to " + reservation.getEndDate());
        System.out.println("🔢 Order: " + reservation.getReservationOrder() + " | 📩 Notification sent? " + reservation.getUnavailableNotificationSent());
    }

    @Test
    @DisplayName("🔄 Should allow updating active and notification flags")
    void shouldUpdateStatusFlags() {
        Reservation reservation = Reservation.builder().build();

        reservation.setActive(false);
        reservation.setUnavailableNotificationSent(true);

        assertThat(reservation.getActive()).isFalse();
        assertThat(reservation.getUnavailableNotificationSent()).isTrue();

        System.out.println("🔁 Reservation marked as inactive.");
        System.out.println("📬 Unavailable notification sent status updated to true.");
    }
}
