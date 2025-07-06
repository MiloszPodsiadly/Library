package com.kodilla.library.repository;

import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatistics;
import com.kodilla.library.model.BookStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookStatisticsRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStatisticsRepository bookStatisticsRepository;

    private Book book;

    @BeforeEach
    void setup() {
        book = Book.builder()
                .title("Domain-Driven Design")
                .author("Eric Evans")
                .isbn("9999999999")
                .available(true)
                .statuses(new HashSet<>(Set.of(BookStatus.AVAILABLE)))
                .build();

        book = bookRepository.save(book);

        BookStatistics stats = BookStatistics.builder()
                .book(book)
                .loanCount(3L)
                .reservationCount(2L)
                .averageRating(4.5)
                .build();

        bookStatisticsRepository.save(stats);
        System.out.println("📊 BookStatistics saved for book: " + book.getTitle());
    }

    @Test
    @DisplayName("🔍 Should find BookStatistics by Book ID")
    void shouldFindByBookId() {
        Optional<BookStatistics> result = bookStatisticsRepository.findByBook_IdBook(book.getIdBook());

        assertThat(result).isPresent();
        assertThat(result.get().getLoanCount()).isEqualTo(3L);
        System.out.println("✅ Found statistics for book ID: " + book.getIdBook());
    }

    @Test
    @DisplayName("🗑️ Should delete BookStatistics")
    void shouldDeleteBookStatistics() {
        BookStatistics stats = bookStatisticsRepository.findByBook_IdBook(book.getIdBook()).orElseThrow();
        bookStatisticsRepository.delete(stats);

        Optional<BookStatistics> result = bookStatisticsRepository.findByBook_IdBook(book.getIdBook());
        assertThat(result).isEmpty();
        System.out.println("🧹 BookStatistics deleted for book ID: " + book.getIdBook());
    }
}
