package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.mapper.BookMapper;
import com.kodilla.library.model.Book;
import com.kodilla.library.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(bookMapper.toDtoList(books));
    }

    @GetMapping("/{idBook}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long idBook)
            throws BookNotFoundByIdException {
        Book book = bookService.getBookById(idBook);
        return ResponseEntity.ok(bookMapper.toDto(book));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String keyword) {
        List<Book> books = bookService.searchByTitle(keyword);
        return ResponseEntity.ok(bookMapper.toDtoList(books));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO>> searchBooksByAuthor(@RequestParam String keyword) {
        List<Book> books = bookService.searchByAuthor(keyword);
        return ResponseEntity.ok(bookMapper.toDtoList(books));
    }



    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        Book savedBook = bookService.addBook(bookMapper.toEntity(bookDTO));
        return ResponseEntity.ok(bookMapper.toDto(savedBook));
    }

    @PutMapping("/{idBook}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long idBook, @RequestBody BookDTO bookDTO)
            throws BookNotFoundByIdException {
        Book updated = bookService.updateBook(idBook, bookMapper.toEntity(bookDTO));
        return ResponseEntity.ok(bookMapper.toDto(updated));
    }

    @DeleteMapping("/{idBook}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long idBook)
            throws BookNotFoundByIdException {
        bookService.deleteBook(idBook);
        return ResponseEntity.noContent().build();
    }
}
