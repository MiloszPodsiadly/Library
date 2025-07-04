package com.kodilla.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    @Test
    @DisplayName("📝 Should correctly build a review")
    void shouldBuildReview() {
        User user = User.builder()
                .idUser(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("Effective Java")
                .build();

        Review review = Review.builder()
                .idReview(10L)
                .user(user)
                .book(book)
                .rating(5)
                .comment("Fantastic read!")
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(review.getIdReview()).isEqualTo(10L);
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getBook()).isEqualTo(book);
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getComment()).isEqualTo("Fantastic read!");
        assertThat(review.getCreatedAt()).isNotNull();

        System.out.println("✅ Review successfully built for book: " + review.getBook().getTitle());
        System.out.println("👤 Reviewer: " + review.getUser().getName());
        System.out.println("⭐ Rating: " + review.getRating());
        System.out.println("💬 Comment: " + review.getComment());
    }

    @Test
    @DisplayName("🔍 Should allow null comment and rating")
    void shouldAllowNullFields() {
        Review review = Review.builder()
                .build();

        assertThat(review.getRating()).isNull();
        assertThat(review.getComment()).isNull();
        assertThat(review.getUser()).isNull();
        assertThat(review.getBook()).isNull();

        System.out.println("⚠️ Review with null fields created (e.g., for drafts or test cases).");
    }
}
