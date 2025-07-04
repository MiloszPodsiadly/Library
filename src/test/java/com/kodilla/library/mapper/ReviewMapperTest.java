package com.kodilla.library.mapper;

import com.kodilla.library.dto.ReviewDTO;
import com.kodilla.library.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReviewMapperTest {

    private ReviewMapper mapper;
    private Review review;

    @BeforeEach
    void setup() {
        mapper = new ReviewMapper();

        User user = User.builder()
                .idUser(1L)
                .email("bob@example.com")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("The Pragmatic Programmer")
                .statuses(new HashSet<>())
                .build();

        review = Review.builder()
                .idReview(3L)
                .user(user)
                .book(book)
                .rating(5)
                .comment("Excellent read!")
                .createdAt(LocalDateTime.of(2024, 6, 1, 12, 0))
                .build();

        System.out.println("🔧 Initialized test data for ReviewMapper.");
    }

    @Test
    @DisplayName("📝 Should map Review to ReviewDTO")
    void shouldMapToDto() {
        ReviewDTO dto = mapper.toDto(review);

        assertThat(dto).isNotNull();
        assertThat(dto.idReview()).isEqualTo(3L);
        assertThat(dto.idUser()).isEqualTo(1L);
        assertThat(dto.idBook()).isEqualTo(2L);
        assertThat(dto.rating()).isEqualTo(5);
        assertThat(dto.comment()).isEqualTo("Excellent read!");
        assertThat(dto.createdAt()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 0));

        System.out.println("✅ Successfully mapped Review to ReviewDTO.");
    }

    @Test
    @DisplayName("📚 Should map list of Reviews to list of DTOs")
    void shouldMapReviewListToDtoList() {
        List<ReviewDTO> result = mapper.toDtoList(List.of(review));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idReview()).isEqualTo(3L);

        System.out.println("📋 Successfully mapped Review list to DTO list.");
    }
}
