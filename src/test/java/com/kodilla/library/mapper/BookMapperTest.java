package com.kodilla.library.mapper;

import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class BookMapperTest {

    private BookMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BookMapper();
        System.out.println("🔧 BookMapper initialized.");
    }

    @Test
    @DisplayName("📘 Should map Book to BookDTO")
    void shouldMapBookToDto() {
        Book book = Book.builder()
                .idBook(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .available(true)
                .statuses(Set.of(BookStatus.AVAILABLE))
                .build();

        BookDTO dto = mapper.toDto(book);

        assertThat(dto).isNotNull();
        assertThat(dto.idBook()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Clean Code");
        assertThat(dto.available()).isTrue();
        assertThat(dto.statuses()).contains(BookStatus.AVAILABLE);

        System.out.println("✅ Book -> DTO mapping successful: " + dto);
    }

    @Test
    @DisplayName("📗 Should map BookDTO to Book")
    void shouldMapDtoToBook() {
        BookDTO dto = new BookDTO(
                2L,
                "Effective Java",
                "Joshua Bloch",
                "9780134685991",
                false,
                Set.of(BookStatus.LOANED)
        );

        Book book = mapper.toEntity(dto);

        assertThat(book).isNotNull();
        assertThat(book.getIdBook()).isEqualTo(2L);
        assertThat(book.getTitle()).isEqualTo("Effective Java");
        assertThat(book.getAvailable()).isFalse();
        assertThat(book.getStatuses()).contains(BookStatus.LOANED);

        System.out.println("✅ DTO -> Book mapping successful: " + book.getTitle());
    }

    @Test
    @DisplayName("📚 Should map list of Books to list of DTOs")
    void shouldMapListOfBooksToDtoList() {
        List<Book> books = List.of(
                Book.builder().idBook(1L).title("Book A").statuses(Set.of(BookStatus.AVAILABLE)).build(),
                Book.builder().idBook(2L).title("Book B").statuses(Set.of(BookStatus.RESERVED)).build()
        );

        List<BookDTO> dtos = mapper.toDtoList(books);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).title()).isEqualTo("Book A");
        assertThat(dtos.get(1).statuses()).contains(BookStatus.RESERVED);

        System.out.println("✅ List<Book> -> List<BookDTO> mapping successful. Count: " + dtos.size());
    }

    @Test
    @DisplayName("❗ Should handle null values")
    void shouldHandleNulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toEntity(null)).isNull();
        System.out.println("⚠️ Null mapping handled gracefully.");
    }
}
