package com.kodilla.library.repository;

import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book1, book2, book3;

    @BeforeEach
    void setup() {
        book1 = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("1111111111")
                .available(true)
                .statuses(new HashSet<>(List.of(BookStatus.AVAILABLE)))
                .build();

        book2 = Book.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("2222222222")
                .available(true)
                .statuses(new HashSet<>(List.of(BookStatus.AVAILABLE)))
                .build();

        book3 = Book.builder()
                .title("Java Concurrency in Practice")
                .author("Brian Goetz")
                .isbn("3333333333")
                .available(false)
                .statuses(new HashSet<>(List.of(BookStatus.NOT_RETURNED)))
                .build();

        bookRepository.saveAll(List.of(book1, book2, book3));
        System.out.println("📚 Test books saved to repository.");
    }

    @Test
    @DisplayName("🔍 Should find books by title (case-insensitive)")
    void shouldFindByTitleIgnoreCase() {
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("java");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle).contains("Effective Java", "Java Concurrency in Practice");

        System.out.println("🔎 Found books with 'java' in title: " + result.size());
    }

    @Test
    @DisplayName("✍️ Should find books by author (case-insensitive)")
    void shouldFindByAuthorIgnoreCase() {
        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("martin");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");

        System.out.println("👤 Found books by author 'martin': " + result.size());
    }

    @Test
    @DisplayName("📦 Should return all books from repository")
    void shouldReturnAllBooks() {
        List<Book> result = bookRepository.findAll();

        assertThat(result).hasSize(3);
        System.out.println("📦 All books retrieved: " + result.size());
    }
}
