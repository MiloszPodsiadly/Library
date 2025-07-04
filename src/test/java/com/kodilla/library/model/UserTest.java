package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("👤 Should build User with default inactive status")
    void shouldBuildUserWithDefaults() {
        User user = User.builder()
                .idUser(1L)
                .name("John Doe")
                .email("john@example.com")
                .passwordHash("secret123")
                .build();

        assertThat(user.getIdUser()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("secret123");
        assertThat(user.getActive()).isFalse(); // default value
        assertThat(user.getToken()).isNull();
        assertThat(user.getLoans()).isNull();

        System.out.println("✅ User created: " + user.getName() + " (Active: " + user.getActive() + ")");
    }

    @Test
    @DisplayName("🔐 Should support setting token details")
    void shouldSetTokenFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .token("ABC123")
                .tokenCreatedAt(now)
                .tokenExpiresAt(now.plusHours(1))
                .build();

        assertThat(user.getToken()).isEqualTo("ABC123");
        assertThat(user.getTokenCreatedAt()).isEqualTo(now);
        assertThat(user.getTokenExpiresAt()).isEqualTo(now.plusHours(1));

        System.out.println("🔐 Token set for user. Created at: " + user.getTokenCreatedAt());
    }

    @Test
    @DisplayName("📚 Should handle user relations like loans, reservations and reviews")
    void shouldAssignRelatedEntities() {
        Loan loan = Loan.builder().idLoan(1L).build();
        Reservation reservation = Reservation.builder().idReservation(1L).build();
        Review review = Review.builder().idReview(1L).build();

        User user = User.builder()
                .loans(List.of(loan))
                .reservations(List.of(reservation))
                .reviews(List.of(review))
                .build();

        assertThat(user.getLoans()).hasSize(1);
        assertThat(user.getReservations()).hasSize(1);
        assertThat(user.getReviews()).hasSize(1);

        System.out.println("📦 User has loans, reservations, and reviews assigned.");
    }
}
