package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookStatisticsTest {

    @Test
    @DisplayName("📊 Should build with default values")
    void shouldBuildWithDefaults() {
        Book book = Book.builder().idBook(1L).title("Test Book").build();

        BookStatistics stats = BookStatistics.builder()
                .book(book)
                .build();

        assertThat(stats.getLoanCount()).isEqualTo(1L);
        assertThat(stats.getReservationCount()).isEqualTo(1L);
        assertThat(stats.getAverageRating()).isEqualTo(0.0);
        assertThat(stats.getBook().getTitle()).isEqualTo("Test Book");

        System.out.println("✅ Built BookStatistics with defaults and book: " + stats.getBook().getTitle());
    }

    @Test
    @DisplayName("🛠️ Should allow setting custom statistics")
    void shouldBuildWithCustomValues() {
        Book book = Book.builder().idBook(2L).title("Clean Architecture").build();

        BookStatistics stats = BookStatistics.builder()
                .book(book)
                .loanCount(10L)
                .reservationCount(5L)
                .averageRating(4.7)
                .build();

        assertThat(stats.getLoanCount()).isEqualTo(10L);
        assertThat(stats.getReservationCount()).isEqualTo(5L);
        assertThat(stats.getAverageRating()).isEqualTo(4.7);
        assertThat(stats.getBook().getTitle()).isEqualTo("Clean Architecture");

        System.out.println("📈 Custom BookStatistics created for: " + stats.getBook().getTitle());
    }
}
