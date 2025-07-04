package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.model.*;
import com.kodilla.library.repository.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private ReservationService reservationService;

    private User user;
    private Book book;
    private Reservation reservation;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().idUser(1L).email("alice@example.com").build();
        book = Book.builder().idBook(2L).title("Clean Code").statuses(new HashSet<>()).build();

        reservation = Reservation.builder()
                .idReservation(3L)
                .user(user)
                .book(book)
                .active(true)
                .createdAt(LocalDateTime.now().minusMinutes(30))
                .startDate(LocalDateTime.now().plusHours(1))
                .endDate(LocalDateTime.now().plusHours(7))
                .build();

        System.out.println("🔧 Initialized mocks for ReservationService.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ ReservationService test finished.\n");
    }

    @Test
    @DisplayName("📚 Should reserve a book successfully")
    void shouldReserveBook() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.existsByUser_IdUserAndBook_IdBookAndActiveTrue(1L, 2L)).thenReturn(false);
        when(reservationRepository.countByBook_IdBook(2L)).thenReturn(0L);
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.reserveBook(1L, 2L);

        assertThat(result).isNotNull();
        assertThat(result.getActive()).isTrue();
        System.out.println("📝 Reservation created for book: " + result.getBook().getTitle());
    }

    @Test
    @DisplayName("🚫 Should throw when user already reserved this book")
    void shouldThrowWhenAlreadyReserved() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.existsByUser_IdUserAndBook_IdBookAndActiveTrue(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> reservationService.reserveBook(1L, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already has an active reservation");

        System.out.println("⚠️ Reservation blocked: already exists.");
    }

    @Test
    @DisplayName("❌ Should throw if reservation is too old to cancel")
    void shouldThrowWhenCancelingOldReservation() {
        reservation = Reservation.builder()
                .idReservation(3L)
                .user(user)
                .book(book)
                .active(true)
                .createdAt(LocalDateTime.now().minusHours(2))
                .startDate(LocalDateTime.now().plusHours(1))
                .endDate(LocalDateTime.now().plusHours(7))
                .build();

        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancelReservation(3L))
                .isInstanceOf(ReservationNotAllowedException.class)
                .hasMessageContaining("cannot be canceled");

        System.out.println("❌ Cancel rejected: reservation too old.");
    }

    @Test
    @DisplayName("🗑️ Should delete reservation created less than 1 hour ago")
    void shouldDeleteRecentActiveReservation() {
        reservation.setActive(true);
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));

        reservationService.deleteReservation(3L);

        verify(reservationRepository).delete(reservation);
        System.out.println("🗑️ Active reservation deleted successfully.");
    }

    @Test
    @DisplayName("⛔ Should block deletion of active reservation older than 1 hour")
    void shouldNotDeleteOldActiveReservation() {
        reservation = Reservation.builder()
                .idReservation(3L)
                .user(user)
                .book(book)
                .active(true)
                .createdAt(LocalDateTime.now().minusHours(2))
                .startDate(LocalDateTime.now().plusHours(1))
                .endDate(LocalDateTime.now().plusHours(7))
                .build();

        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.deleteReservation(3L))
                .isInstanceOf(ReservationNotAllowedException.class);

        System.out.println("⛔ Deletion blocked: active reservation too old.");
    }


    @Test
    @DisplayName("🔍 Should return all reservations for user")
    void shouldReturnUserReservations() throws UserNotFoundByIdException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findAllByUser_IdUser(1L)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.getReservationsByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBook().getTitle()).isEqualTo("Clean Code");
        System.out.println("📃 Found reservation(s) for user: " + result.size());
    }

    @Test
    @DisplayName("📘 Should return reservations for given book")
    void shouldReturnReservationsForBook() throws BookNotFoundByIdException {
        when(bookRepository.existsById(2L)).thenReturn(true);
        when(reservationRepository.findAllByBook_IdBook(2L)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.getReservationsForBook(2L);

        assertThat(result).hasSize(1);
        System.out.println("📘 Book has " + result.size() + " reservation(s).");
    }
}
