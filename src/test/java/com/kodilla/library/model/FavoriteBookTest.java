package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteBookTest {

    @Test
    @DisplayName("🌟 Should create FavoriteBook with book and user")
    void shouldCreateFavoriteBook() {
        User user = User.builder()
                .idUser(1L)
                .email("anna@example.com")
                .name("Anna")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("Effective Java")
                .author("Joshua Bloch")
                .build();

        LocalDateTime now = LocalDateTime.now();

        FavoriteBook favorite = FavoriteBook.builder()
                .user(user)
                .book(book)
                .addedAt(now)
                .build();

        assertThat(favorite.getUser().getEmail()).isEqualTo("anna@example.com");
        assertThat(favorite.getBook().getTitle()).isEqualTo("Effective Java");
        assertThat(favorite.getAddedAt()).isEqualTo(now);

        System.out.println("📚 FavoriteBook mapped: " + favorite.getBook().getTitle() + " by user " + favorite.getUser().getName());
    }

    @Test
    @DisplayName("🕓 Should allow null addedAt if not set explicitly")
    void shouldAllowNullTimestamp() {
        FavoriteBook favorite = FavoriteBook.builder()
                .user(User.builder().idUser(1L).build())
                .book(Book.builder().idBook(2L).build())
                .build();

        assertThat(favorite.getAddedAt()).isNull();
        System.out.println("ℹ️ addedAt is null as expected.");
    }
}
