package com.kodilla.library.service;

import java.util.List;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatistics;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.BookStatisticsRepository;
import com.kodilla.library.repository.FavoriteBookRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.ReservationRepository;
import com.kodilla.library.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookStatisticsService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteBookRepository favoriteBookRepository;
    private final BookStatisticsRepository bookStatisticsRepository;

    public List<Book> getAllBooksSortedByLoanCountDesc() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> Long.compare(
                        loanRepository.countByBook_IdBook(b2.getIdBook()),
                        loanRepository.countByBook_IdBook(b1.getIdBook())
                ))
                .toList();
    }

    public List<Book> getTop10MostLoanedBooks() {
        return getAllBooksSortedByLoanCountDesc()
                .stream()
                .limit(10)
                .toList();
    }

    public List<Book> getTop3ReservedBooks() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> Long.compare(
                        reservationRepository.countByBook_IdBook(b2.getIdBook()),
                        reservationRepository.countByBook_IdBook(b1.getIdBook())
                ))
                .limit(3)
                .toList();
    }

    public List<Book> getTop3FavoriteBooks() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> Long.compare(
                        favoriteBookRepository.countByBook_IdBook(b2.getIdBook()),
                        favoriteBookRepository.countByBook_IdBook(b1.getIdBook())
                ))
                .limit(3)
                .toList();
    }

    public Long getLoanCountForBook(Long idBook) {
        return loanRepository.countByBook_IdBook(idBook);
    }

    public BookStatistics getStatisticsByBookId(Long idBook) {
        return bookStatisticsRepository.findByBook_IdBook(idBook)
                .orElse(null);
    }

    public Long getReservationCountForBook(Long idBook) {
        return reservationRepository.countByBook_IdBook(idBook);
    }

    public Double getAverageRatingForBook(Long idBook) {
        return reviewRepository.findAverageRatingByBookId(idBook);
    }

    public String getFavoriteCountForBook(Long idBook) {
        return "How many likes it has: " + favoriteBookRepository.countByBook_IdBook(idBook);
    }


    public Book getBookById(Long idBook) throws BookNotFoundByIdException {
        return bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));
    }
}
