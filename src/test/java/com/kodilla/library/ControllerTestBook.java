package com.kodilla.library;

import com.kodilla.library.dto.BookDto;
import com.kodilla.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.kodilla.library.controller.BookController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerTestBook {

    private BookService bookService;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class); // Mockowanie serwisu
        bookController = new BookController(bookService); // Tworzenie instancji kontrolera
    }

    @Test
    void shouldAddBook() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");

        BookDto createdBookDto = new BookDto();
        createdBookDto.setId(1L);
        createdBookDto.setTitle("Test Book");
        createdBookDto.setAuthor("Test Author");

        when(bookService.addBook(bookDto.getTitle(), bookDto.getAuthor())).thenReturn(createdBookDto);

        // When
        ResponseEntity<BookDto> response = bookController.addBook(bookDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdBookDto, response.getBody());
        verify(bookService, times(1)).addBook("Test Book", "Test Author");
    }

    @Test
    void shouldMarkAsUnavailable() {
        // Given
        Long bookId = 1L;
        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(bookId);
        updatedBookDto.setAvailable(false);

        when(bookService.markAsUnavailable(bookId)).thenReturn(updatedBookDto);

        // When
        ResponseEntity<BookDto> response = bookController.markAsUnavailable(bookId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBookDto, response.getBody());
        verify(bookService, times(1)).markAsUnavailable(bookId);
    }
}