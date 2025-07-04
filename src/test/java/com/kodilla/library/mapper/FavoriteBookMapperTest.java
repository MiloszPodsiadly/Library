package com.kodilla.library.mapper;

import com.kodilla.library.dto.FavoriteBookDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.FavoriteBook;
import com.kodilla.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FavoriteBookMapperTest {

    private FavoriteBookMapper mapper;
    private User user;
    private Book book;
    private FavoriteBook favorite;

    @BeforeEach
    void setUp() {
        mapper = new FavoriteBookMapper();

        user = User.builder()
                .idUser(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        book = Book.builder()
                .idBook(2L)
                .title("Effective Java")
                .build();

        favorite = FavoriteBook.builder()
                .idFavoriteBook(10L)
                .user(user)
                .book(book)
                .addedAt(LocalDateTime.of(2024, 5, 1, 10, 0))
                .build();

        System.out.println("🔧 FavoriteBookMapper test setup complete.");
    }

    @Test
    @DisplayName("📘 Should map FavoriteBook to DTO")
    void shouldMapToDto() {
        FavoriteBookDTO dto = mapper.toDto(favorite);

        assertThat(dto).isNotNull();
        assertThat(dto.idFavoriteBook()).isEqualTo(10L);
        assertThat(dto.idUser()).isEqualTo(1L);
        assertThat(dto.idBook()).isEqualTo(2L);
        assertThat(dto.addedAt()).isEqualTo(LocalDateTime.of(2024, 5, 1, 10, 0));

        System.out.println("✅ Mapped single FavoriteBook to DTO successfully.");
    }

    @Test
    @DisplayName("📚 Should map list of FavoriteBooks to DTOs")
    void shouldMapListToDtoList() {
        List<FavoriteBookDTO> result = mapper.toDtoList(List.of(favorite));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idFavoriteBook()).isEqualTo(10L);

        System.out.println("📚 Mapped FavoriteBook list to DTO list successfully.");
    }
}
