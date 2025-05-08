package com.kodilla.library;

import com.kodilla.library.dto.ReservationDto;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.ReservationRepository;
import com.kodilla.library.repository.UserRepository;
import com.kodilla.library.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTestSuite {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateReservation() {
        // Przygotowanie
        User user = new User(1L, "John Doe","user","user");
        Book book = new Book(1L, "Test Book", "Test", true);
        Reservation reservation = new Reservation(1L, book, user, LocalDate.now());

        when(bookRepository.findByIdAndAvailableTrue(book.getId())).thenReturn(Optional.of(book));
        when(reservationRepository.findByBookAndUser(book, user)).thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Akcja
        ReservationDto result = reservationService.createReservation(user, book.getId());

        // Weryfikacja
        assertNotNull(result);
        assertEquals(book.getId(), result.getBookId());
        assertEquals(user.getId(), result.getUserId());
        verify(bookRepository, times(1)).findByIdAndAvailableTrue(book.getId());
        verify(reservationRepository, times(1)).findByBookAndUser(book, user);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenBookNotAvailable() {
        // Przygotowanie
        Long bookId = 1L;
        User user = new User(1L, "John Doe","user","user");

        when(bookRepository.findByIdAndAvailableTrue(bookId)).thenReturn(Optional.empty());

        // Akcja i weryfikacja
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(user, bookId)
        );

        assertEquals("Book not available", exception.getMessage());
        verify(bookRepository, times(1)).findByIdAndAvailableTrue(bookId);
    }

    @Test
    void shouldThrowExceptionIfAlreadyReserved() {
        // Przygotowanie
        User user = new User(1L, "John Doe","user","user");
        Book book = new Book(1L, "Test Book", "Author Name", true);
        Reservation reservation = new Reservation(1L, book, user, LocalDate.now());

        when(bookRepository.findByIdAndAvailableTrue(book.getId())).thenReturn(Optional.of(book));
        when(reservationRepository.findByBookAndUser(book, user)).thenReturn(Optional.of(reservation));

        // Akcja i weryfikacja
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(user, book.getId())
        );

        assertEquals("You have already reserved this book", exception.getMessage());
        verify(bookRepository, times(1)).findByIdAndAvailableTrue(book.getId());
        verify(reservationRepository, times(1)).findByBookAndUser(book, user);
    }

    @Test
    void shouldGetReservationsForBook() {
        // Przygotowanie
        Book book = new Book(1L, "Test Book","Author Name", true);
        Reservation reservation = new Reservation(1L, book, new User(2L, "John Doe","user","user"), LocalDate.now());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(reservationRepository.findByBookAndReservationDateAfter(book, LocalDate.now()))
                .thenReturn(List.of(reservation));

        // Akcja
        List<ReservationDto> result = reservationService.getReservationsForBook(book.getId());

        // Weryfikacja
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getId(), result.get(0).getBookId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(reservationRepository, times(1))
                .findByBookAndReservationDateAfter(book, LocalDate.now());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        // Przygotowanie
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Akcja i weryfikacja
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reservationService.getReservationsForBook(bookId)
        );

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void shouldGetUserReservations() {
        // Przygotowanie
        User user = new User(1L, "John Doe","user","user");
        Reservation reservation = new Reservation(1L, new Book(2L, "Book Name","Author", true), user, LocalDate.now());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(reservationRepository.findByUser(user)).thenReturn(List.of(reservation));

        // Akcja
        List<ReservationDto> result = reservationService.getUserReservations(user.getId());

        // Weryfikacja
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUserId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(reservationRepository, times(1)).findByUser(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Przygotowanie
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Akcja i weryfikacja
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reservationService.getUserReservations(userId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}
