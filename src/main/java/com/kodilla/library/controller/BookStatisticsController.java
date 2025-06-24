package com.kodilla.library.controller;

import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.dto.BookStatisticsDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.mapper.BookMapper;
import com.kodilla.library.mapper.BookStatisticsMapper;
import com.kodilla.library.model.Book;
import com.kodilla.library.service.BookStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.kodilla.library.model.BookStatistics;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books/statistics")
public class BookStatisticsController {

    private final BookStatisticsService bookStatisticsService;
    private final BookStatisticsMapper bookStatisticsMapper;
    private final BookMapper bookMapper;

    @GetMapping("/{id}")
    public ResponseEntity<BookStatisticsDTO> getStatisticsForBook(@PathVariable Long id) {
        BookStatistics stats = bookStatisticsService.getStatisticsByBookId(id);

        if (stats == null) {
            return ResponseEntity.noContent().build(); // albo 404
        }

        BookStatisticsDTO dto = bookStatisticsMapper.toDto(stats);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/top-loaned")
    public ResponseEntity<List<BookDTO>> getTop10LoanedBooks() {
        List<Book> topBooks = bookStatisticsService.getTop10MostLoanedBooks();
        return ResponseEntity.ok(bookMapper.toDtoList(topBooks));
    }

    @GetMapping("/top-reserved")
    public ResponseEntity<List<BookDTO>> getTop3ReservedBooks() {
        List<Book> topBooks = bookStatisticsService.getTop3ReservedBooks();
        return ResponseEntity.ok(bookMapper.toDtoList(topBooks));
    }

    @GetMapping("/top-favorite")
    public ResponseEntity<List<BookDTO>> getTop3FavoriteBooks() {
        List<Book> topBooks = bookStatisticsService.getTop3FavoriteBooks();
        return ResponseEntity.ok(bookMapper.toDtoList(topBooks));
    }

    @GetMapping("/{id}/loans")
    public ResponseEntity<Long> getLoanCountForBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStatisticsService.getLoanCountForBook(id));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<Long> getReservationCountForBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStatisticsService.getReservationCountForBook(id));
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getAverageRatingForBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStatisticsService.getAverageRatingForBook(id));
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<Long> getFavoriteCountForBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookStatisticsService.getFavoriteCountForBook(id));
    }
}
