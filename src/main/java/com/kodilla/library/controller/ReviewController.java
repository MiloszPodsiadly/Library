package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodilla.library.dto.ReviewDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.ReviewNotFoundByIdException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.ReviewMapper;
import com.kodilla.library.model.Review;
import com.kodilla.library.service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(
            @RequestParam Long idUser,
            @RequestParam Long idBook,
            @RequestParam String comment,
            @RequestParam int rating
    ) throws BookNotFoundByIdException, UserNotFoundByIdException {
        Review review = reviewService.addReview(idUser, idBook, comment, rating);
        return ResponseEntity.ok(reviewMapper.toDto(review));
    }

    @DeleteMapping("/{idReview}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long idReview)
            throws ReviewNotFoundByIdException {
        reviewService.deleteReview(idReview);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{idBook}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForBook(@PathVariable Long idBook)
            throws BookNotFoundByIdException {
        List<Review> reviews = reviewService.getReviewsForBook(idBook);
        return ResponseEntity.ok(reviewMapper.toDtoList(reviews));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        List<Review> reviews = reviewService.getUserReviews(idUser);
        return ResponseEntity.ok(reviewMapper.toDtoList(reviews));
    }
}
