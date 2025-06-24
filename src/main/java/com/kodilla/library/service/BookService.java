package com.kodilla.library.service;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

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

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long idBook, Book updatedBook) throws BookNotFoundByIdException {
        Book existing = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

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
}

