package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FineTest {

    @Test
    @DisplayName("hould💰 S create Fine with default unpaid status")
    void shouldCreateUnpaidFine() {
        User user = User.builder()
                .idUser(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        Loan loan = Loan.builder()
                .idLoan(10L)
                .returned(false)
                .build();

        LocalDateTime issued = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("5.00");

        Fine fine = Fine.builder()
                .user(user)
                .loan(loan)
                .amount(amount)
                .reason("Overdue")
                .issuedDate(issued)
                .build();

        assertThat(fine.getUser().getEmail()).isEqualTo("alice@example.com");
        assertThat(fine.getLoan().getIdLoan()).isEqualTo(10L);
        assertThat(fine.getAmount()).isEqualByComparingTo("5.00");
        assertThat(fine.getReason()).isEqualTo("Overdue");
        assertThat(fine.getIssuedDate()).isEqualTo(issued);
        assertThat(fine.getPaid()).isFalse();

        System.out.println("✅ Fine created for user 📧: " + fine.getUser().getEmail());
        System.out.println("💵 Fine amount 💰: " + fine.getAmount() + " | Reason: " + fine.getReason());
        System.out.println("⏰ Issued at: " + fine.getIssuedDate() + " | Paid? " + fine.getPaid());
    }

    @Test
    @DisplayName("✅ Should allow setting Fine as paid")
    void shouldMarkFineAsPaid() {
        Fine fine = Fine.builder()
                .paid(false)
                .build();

        System.out.println("🔄 Marking fine as paid...");
        fine.setPaid(true);

        assertThat(fine.getPaid()).isTrue();
        System.out.println("🎉 Fine successfully marked as paid! 💸");
    }
}
