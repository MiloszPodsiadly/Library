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

    public String getLoanCountForBook(Long idBook) {
        Long count = loanRepository.countByBook_IdBook(idBook);
        return "Loan count for book ID " + idBook + ": " + count;
    }

    public String getReservationCountForBook(Long idBook) {
        Long count = reservationRepository.countByBook_IdBook(idBook);
        return "Reservation count for book ID " + idBook + ": " + count;
    }

    public String getAverageRatingForBook(Long idBook) {
        Double avg = reviewRepository.findAverageRatingByBookId(idBook);
        if (avg == null) {
            return "Average rating for book ID " + idBook + ": No ratings yet";
        }
        return "Average rating for book ID " + idBook + ": " + avg;
    }

    public String getFavoriteCountForBook(Long idBook) {
        Long count = favoriteBookRepository.countByBook_IdBook(idBook);
        return "Number of likes for book ID " + idBook + ": " + count;
    }

    public BookStatistics getStatisticsByBookId(Long idBook) {
        return bookStatisticsRepository.findByBook_IdBook(idBook)
                .orElseGet(() -> {
                    Book book = bookRepository.findById(idBook)
                            .orElseThrow(() -> new BookNotFoundByIdException(idBook));

                    BookStatistics stats = BookStatistics.builder()
                            .book(book)
                            .loanCount(loanRepository.countByBook_IdBook(idBook))
                            .reservationCount(reservationRepository.countByBook_IdBook(idBook))
                            .averageRating(reviewRepository.findAverageRatingByBookId(idBook))
                            .favoriteCount(favoriteBookRepository.countByBook_IdBook(idBook))
                            .build();

                    return bookStatisticsRepository.save(stats);
                });
    }

    public Book getBookById(Long idBook) throws BookNotFoundByIdException {
        return bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));
    }
}
