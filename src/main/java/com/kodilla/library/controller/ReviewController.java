package com.kodilla.library.controller;

import com.kodilla.library.dto.ReviewDTO;
import com.kodilla.library.exception.*;
import com.kodilla.library.mapper.ReviewMapper;
import com.kodilla.library.model.Review;
import com.kodilla.library.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    // ✅ POST /reviews?userId=...&bookId=...
    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam String comment,
            @RequestParam int rating
    ) throws BookNotFoundByIdException, UserNotFoundByIdException {
        Review review = reviewService.addReview(userId, bookId, comment, rating);
        return ResponseEntity.ok(reviewMapper.toDto(review));
    }

    // ✅ DELETE /reviews/{reviewId}
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId)
            throws ReviewNotFoundByIdException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET /reviews/book/{bookId}
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForBook(@PathVariable Long bookId)
            throws BookNotFoundByIdException {
        List<Review> reviews = reviewService.getReviewsForBook(bookId);
        return ResponseEntity.ok(reviewMapper.toDtoList(reviews));
    }

    // ✅ GET /reviews/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(@PathVariable Long userId)
            throws UserNotFoundByIdException {
        List<Review> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviewMapper.toDtoList(reviews));
    }
}
