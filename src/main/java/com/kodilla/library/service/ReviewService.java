package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.model.*;
import com.kodilla.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public Review addReview(Long idUser, Long idBook, String comment, int rating)
            throws BookNotFoundByIdException, UserNotFoundByIdException {

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Review review = Review.builder()
                .book(book)
                .user(user)
                .rating(rating)
                .comment(comment)
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
