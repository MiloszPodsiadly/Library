package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.dto.BookStatisticsDTO;
import com.kodilla.library.mapper.BookMapper;
import com.kodilla.library.mapper.BookStatisticsMapper;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatistics;
import com.kodilla.library.service.BookStatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books/statistics")
public class BookStatisticsController {

    private final BookStatisticsService bookStatisticsService;
    private final BookStatisticsMapper bookStatisticsMapper;
    private final BookMapper bookMapper;

    @GetMapping("/{idBook}")
    public ResponseEntity<BookStatisticsDTO> getStatisticsForBook(@PathVariable Long idBook) {
        BookStatistics stats = bookStatisticsService.getStatisticsByBookId(idBook);
        if (stats == null) {
            return ResponseEntity.noContent().build();
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

    @GetMapping("/loans/{idBook}")
    public ResponseEntity<String> getLoanCount(@PathVariable Long idBook) {
        return ResponseEntity.ok(bookStatisticsService.getLoanCountForBook(idBook));
    }


    @GetMapping("/reservations/{idBook}")
    public ResponseEntity<String> getReservationCountForBook(@PathVariable Long idBook) {
        return ResponseEntity.ok(bookStatisticsService.getReservationCountForBook(idBook));
    }

    @GetMapping("/rating/{idBook}")
    public ResponseEntity<String> getAverageRatingForBook(@PathVariable Long idBook) {
        return ResponseEntity.ok(bookStatisticsService.getAverageRatingForBook(idBook));
    }

    @GetMapping("/favorites/{idBook}")
    public ResponseEntity<String> getFavoriteCountForBook(@PathVariable Long idBook) {
        return ResponseEntity.ok(bookStatisticsService.getFavoriteCountForBook(idBook));
    }
}
