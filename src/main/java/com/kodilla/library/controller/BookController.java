package com.kodilla.library.controller;

import com.kodilla.library.dto.BookDto;
import com.kodilla.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.addBook(bookDto.getTitle(), bookDto.getAuthor());
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/unavailable")
    public ResponseEntity<BookDto> markAsUnavailable(@PathVariable Long id) {
        BookDto updatedBook = bookService.markAsUnavailable(id);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }
}
