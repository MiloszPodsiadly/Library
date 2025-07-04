package com.kodilla.library.repository;

import com.kodilla.library.model.Book;
import com.kodilla.library.model.Review;
import com.kodilla.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    private User user;
    private Book book;

    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder()
                .name("Reviewer")
                .email("review@example.com")
                .passwordHash("password")
                .active(true)
                .build());

        book = bookRepository.save(Book.builder()
                .title("Review Book")
                .author("Author")
                .isbn("999-999-999")
                .available(true)
                .build());

        Review r1 = Review.builder()
                .book(book)
                .user(user)
                .rating(5)
                .comment("Excellent!")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        Review r2 = Review.builder()
                .book(book)
                .user(user)
                .rating(3)
                .comment("Not bad")
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.saveAll(List.of(r1, r2));
        System.out.println("🧪 Setup complete: 2 reviews saved for the same book.");
    }

    @Test
    @DisplayName("📚 Should find reviews by book ID")
    void shouldFindReviewsByBookId() {
        List<Review> reviews = reviewRepository.findByBook_IdBook(book.getIdBook());

        assertThat(reviews).hasSize(2);
        System.out.println("📚 Reviews found for book: " + reviews.size());
    }

    @Test
    @DisplayName("👤 Should find reviews by user ID")
    void shouldFindReviewsByUserId() {
        List<Review> reviews = reviewRepository.findByUser_IdUser(user.getIdUser());

        assertThat(reviews).hasSize(2);
        System.out.println("👤 Reviews found for user: " + reviews.size());
    }

    @Test
    @DisplayName("⭐ Should calculate average rating for a book")
    void shouldCalculateAverageRating() {
        Double average = reviewRepository.findAverageRatingByBookId(book.getIdBook());

        assertThat(average).isNotNull();
        assertThat(average).isEqualTo(4.0);
        System.out.println("⭐ Average rating for book: " + average);
    }
}
