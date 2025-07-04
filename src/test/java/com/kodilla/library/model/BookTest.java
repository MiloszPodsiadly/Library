package com.kodilla.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setup() {
        book = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("978-0134685991")
                .build();
        System.out.println("📚 Initialized Book object for testing.");
    }

    @Test
    @DisplayName("✅ Should build Book with default availability and status")
    void shouldBuildWithDefaults() {
        assertThat(book.getAvailable()).isTrue();
        assertThat(book.getStatuses()).containsExactly(BookStatus.AVAILABLE);
        System.out.println("🎯 Book has default availability and status.");
    }

    @Test
    @DisplayName("🛠️ Should allow setting title, author, and isbn via setters")
    void shouldAllowFieldMutationViaSetters() {
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setIsbn("978-0132350884");

        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getIsbn()).isEqualTo("978-0132350884");

        System.out.println("📝 Title, author and ISBN updated successfully.");
    }

    @Test
    @DisplayName("📖 Should support setting and getting statuses")
    void shouldSupportStatuses() {
        book.getStatuses().clear();
        book.getStatuses().add(BookStatus.RESERVED);
        assertThat(book.getStatuses()).containsExactly(BookStatus.RESERVED);
        System.out.println("🔁 Status updated to RESERVED.");
    }

    @Test
    @DisplayName("🔗 Should allow assigning loans, reservations, and reviews")
    void shouldSupportRelatedEntities() {
        Loan loan = Loan.builder().build();
        Reservation reservation = Reservation.builder().build();
        Review review = Review.builder().build();

        book = book.builder()
                .loans(List.of(loan))
                .reservations(List.of(reservation))
                .reviews(List.of(review))
                .build();

        assertThat(book.getLoans()).hasSize(1);
        assertThat(book.getReservations()).hasSize(1);
        assertThat(book.getReviews()).hasSize(1);

        System.out.println("🔗 Book linked to loan, reservation, and review.");
    }
}
