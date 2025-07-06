package com.kodilla.library.service;

import java.util.List;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long idBook) throws BookNotFoundByIdException {
        return bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));
    }
    public List<Book> searchByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Book> searchByAuthor(String keyword) {
        return bookRepository.findByAuthorContainingIgnoreCase(keyword);
    }
    @Transactional
    public Book addBook(Book book) {
        validateBookFields(book);

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with this ISBN already exists. Please use a unique ISBN.");
        }

        return bookRepository.save(book);
    }
    @Transactional
    public Book updateBook(Long idBook, Book updatedBook) {
        validateBookFields(updatedBook);

        Book existing = bookRepository.findById(idBook)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + idBook));

        bookRepository.findByIsbn(updatedBook.getIsbn()).ifPresent(bookWithSameIsbn -> {
            if (!bookWithSameIsbn.getIdBook().equals(idBook)) {
                throw new IllegalArgumentException("Another book with this ISBN already exists.");
            }
        });

        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setAvailable(updatedBook.getAvailable());

        existing.getStatuses().clear();
        existing.getStatuses().addAll(updatedBook.getStatuses());

        return bookRepository.save(existing);
    }
    public void deleteBook(Long idBook) throws BookNotFoundByIdException {
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));
        bookRepository.delete(book);
    }

    private void validateBookFields(Book book) {
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or blank.");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank.");
        }
    }
}

