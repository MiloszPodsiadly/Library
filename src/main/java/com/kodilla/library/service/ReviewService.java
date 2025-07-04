package com.kodilla.library.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.ReviewNotFoundByIdException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Review;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.ReviewRepository;
import com.kodilla.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public List<Review> getReviewsForBook(Long idBook) throws BookNotFoundByIdException {
        if (!bookRepository.existsById(idBook)) {
            throw new BookNotFoundByIdException(idBook);
        }
        return reviewRepository.findByBook_IdBook(idBook);
    }

    public Review addReview(Long userId, Long bookId, String comment, Integer rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdException(userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundByIdException(bookId));

        boolean alreadyReviewed = reviewRepository.existsByUser_IdUserAndBook_IdBook(userId, bookId);
        if (alreadyReviewed) {
            throw new IllegalStateException("User has already submitted a review for this book.");
        }

        Review review = Review.builder()
                .user(user)
                .book(book)
                .comment(comment)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }


    public void deleteReview(Long idReview) throws ReviewNotFoundByIdException {
        Review review = reviewRepository.findById(idReview)
                .orElseThrow(() -> new ReviewNotFoundByIdException(idReview));
        reviewRepository.delete(review);
    }

    public List<Review> getUserReviews(Long idUser) throws UserNotFoundByIdException {
        if (!userRepository.existsById(idUser)) {
            throw new UserNotFoundByIdException(idUser);
        }
        return reviewRepository.findByUser_IdUser(idUser);
    }
}
