package com.kodilla.library.controller;

import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.mapper.BookMapper;
import com.kodilla.library.model.Book;
import com.kodilla.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id)
            throws BookNotFoundByIdException {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(bookMapper.toDto(book));
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        Book savedBook = bookService.addBook(bookMapper.toEntity(bookDTO));
        return ResponseEntity.ok(bookMapper.toDto(savedBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO)
            throws BookNotFoundByIdException {
        Book updated = bookService.updateBook(id, bookMapper.toEntity(bookDTO));
        return ResponseEntity.ok(bookMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id)
            throws BookNotFoundByIdException {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
