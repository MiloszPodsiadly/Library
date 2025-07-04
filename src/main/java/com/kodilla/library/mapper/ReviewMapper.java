package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.ReviewDTO;
import com.kodilla.library.model.Review;

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

    public List<ReviewDTO> toDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

