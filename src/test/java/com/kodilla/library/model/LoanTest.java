package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoanTest {

    @Test
    @DisplayName("📘 Should create Loan with default values")
    void shouldCreateLoanWithDefaults() {
        User user = User.builder().idUser(1L).name("Alice").build();
        Book book = Book.builder().idBook(2L).title("Clean Code").build();
        LocalDateTime now = LocalDateTime.now();

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(now)
                .returnDate(now.plusDays(1))
                .build();

        assertThat(loan.getReturned()).isFalse();
        assertThat(loan.getExtensionCount()).isEqualTo(0);
        assertThat(loan.getBook().getTitle()).isEqualTo("Clean Code");
        assertThat(loan.getUser().getName()).isEqualTo("Alice");

        System.out.println("📚 Loan created for book: " + loan.getBook().getTitle());
        System.out.println("👤 Borrowed by: " + loan.getUser().getName());
        System.out.println("📅 Loan date: " + loan.getLoanDate() + " | Return date: " + loan.getReturnDate());
        System.out.println("✅ Returned? " + loan.getReturned() + " | 🔁 Extensions: " + loan.getExtensionCount());
    }

    @Test
    @DisplayName("🔁 Should allow updating return status and extension count")
    void shouldUpdateReturnedAndExtension() {
        Loan loan = Loan.builder()
                .returned(false)
                .extensionCount(0)
                .build();

        loan.setReturned(true);
        loan.setExtensionCount(1);

        assertThat(loan.getReturned()).isTrue();
        assertThat(loan.getExtensionCount()).isEqualTo(1);

        System.out.println("🔁 Loan marked as returned.");
        System.out.println("➕ Extension count updated to: " + loan.getExtensionCount());
    }

    @Test
    @DisplayName("💰 Should allow assigning Fine to Loan")
    void shouldAssignFineToLoan() {
        Fine fine = Fine.builder().reason("Overdue").build();
        Loan loan = Loan.builder().build();

        loan.setFine(fine);

        assertThat(loan.getFine().getReason()).isEqualTo("Overdue");
        System.out.println("💰 Fine assigned to loan with reason: " + loan.getFine().getReason());
    }
}
