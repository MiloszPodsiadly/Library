package com.kodilla.library.mapper;

import com.kodilla.library.dto.ReviewDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Review;
import com.kodilla.library.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public ReviewDTO toDto(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDTO(
                review.getIdReview(),
                review.getUser().getIdUser(),
                review.getBook().getIdBook(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    public Review toEntity(ReviewDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }
        return Review.builder()
                .idReview(dto.idReview())
                .user(user)
                .book(book)
                .rating(dto.rating())
                .comment(dto.comment())
                .createdAt(dto.createdAt())
                .build();
    }

    public List<ReviewDTO> toDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

