package com.kodilla.library.mapper;

import com.kodilla.library.dto.BookStatisticsDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BookStatisticsMapperTest {

    private BookStatisticsMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new BookStatisticsMapper();
        System.out.println("🔧 BookStatisticsMapper initialized.");
    }

    @Test
    @DisplayName("📊 Should map BookStatistics to DTO")
    void shouldMapToDto() {
        Book book = Book.builder()
                .idBook(42L)
                .title("Refactoring")
                .build();

        BookStatistics stats = BookStatistics.builder()
                .book(book)
                .loanCount(15L)
                .reservationCount(8L)
                .averageRating(4.3)
                .build();

        BookStatisticsDTO dto = mapper.toDto(stats);

        assertThat(dto).isNotNull();
        assertThat(dto.idBookStatistics()).isEqualTo(42L);
        assertThat(dto.loanCount()).isEqualTo(15L);
        assertThat(dto.reservationCount()).isEqualTo(8L);
        assertThat(dto.averageRating()).isEqualTo(4.3);

        System.out.println("✅ Mapped BookStatistics to DTO successfully: " + dto);
    }

    @Test
    @DisplayName("📈 Should map list of BookStatistics to list of DTOs")
    void shouldMapListToDtoList() {
        BookStatistics stat1 = BookStatistics.builder()
                .book(Book.builder().idBook(1L).build())
                .loanCount(10L)
                .reservationCount(5L)
                .averageRating(4.0)
                .build();

        BookStatistics stat2 = BookStatistics.builder()
                .book(Book.builder().idBook(2L).build())
                .loanCount(7L)
                .reservationCount(2L)
                .averageRating(3.5)
                .build();

        List<BookStatisticsDTO> result = mapper.toDtoList(List.of(stat1, stat2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).idBookStatistics()).isEqualTo(1L);
        assertThat(result.get(1).averageRating()).isEqualTo(3.5);

        System.out.println("✅ Mapped list of BookStatistics to DTOs. Count: " + result.size());
    }

    @Test
    @DisplayName("❗ Should handle null input gracefully")
    void shouldReturnNullForNullInput() {
        BookStatisticsDTO dto = mapper.toDto(null);
        assertThat(dto).isNull();
        System.out.println("⚠️ Null input handled correctly.");
    }
}
