package com.kodilla.library.service;
import com.kodilla.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import com.kodilla.library.dto.BookDto;
import com.kodilla.library.model.Book;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Dodawanie książki
    public BookDto addBook(String title, String author) {
        Book book = new Book(title, author, true);
        Book savedBook = bookRepository.save(book);
        return toDto(savedBook);  // Zwracamy BookDto po zapisaniu książki
    }

    // Oznaczenie książki jako niedostępnej
    public BookDto markAsUnavailable(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setAvailable(false);
        Book savedBook = bookRepository.save(book);
        return toDto(savedBook);  // Zwracamy BookDto po zapisaniu
    }
    // Pomocnicza metoda do konwersji encji Book na BookDto
    private BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.isAvailable()
        );
    }
}

