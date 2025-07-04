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

class LoanServiceTest {

    @Mock private LoanRepository loanRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;
    @Mock private ReservationRepository reservationRepository;

    @InjectMocks private LoanService loanService;

    private User user;
    private Book book;
    private Reservation reservation;
    private Loan loan;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().idUser(1L).name("Alice").build();

        book = Book.builder()
                .idBook(2L)
                .title("Test Book")
                .available(true)
                .statuses(new HashSet<>(Set.of(BookStatus.AVAILABLE)))
                .build();

        reservation = Reservation.builder()
                .user(user)
                .book(book)
                .active(true)
                .startDate(LocalDateTime.now().minusHours(1))
                .endDate(LocalDateTime.now().plusHours(5))
                .build();

        loan = Loan.builder()
                .idLoan(3L)
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now().plusHours(6))
                .returned(false)
                .extensionCount(0)
                .build();

        System.out.println("🔧 LoanService test context initialized.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ LoanService test completed.\n");
    }

    @Test
    @DisplayName("📚 Should loan a book with valid user and active reservation")
    void shouldLoanBook() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.findAllByBook_IdBook(2L)).thenReturn(List.of(reservation));
        when(loanRepository.countByUser_IdUserAndReturnedFalse(1L)).thenReturn(0L);
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.loanBook(1L, 2L);

        assertThat(result.getUser().getIdUser()).isEqualTo(1L);
        assertThat(result.getBook().getIdBook()).isEqualTo(2L);
        assertThat(result.getReturned()).isFalse();

        System.out.println("📦 Loan successfully created for book: " + result.getBook().getTitle());
    }

    @Test
    @DisplayName("🚫 Should throw when user has no active reservation")
    void shouldThrowIfNoActiveReservation() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.findAllByBook_IdBook(2L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> loanService.loanBook(1L, 2L))
                .isInstanceOf(LoanNotAllowedException.class)
                .hasMessageContaining("No active reservation");

        System.out.println("❌ Rejected loan – no active reservation.");
    }

    @Test
    @DisplayName("🛑 Should throw if user exceeds max active loans")
    void shouldRejectLoanIfMaxActiveReached() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(reservationRepository.findAllByBook_IdBook(2L)).thenReturn(List.of(reservation));
        when(loanRepository.countByUser_IdUserAndReturnedFalse(1L)).thenReturn(3L);

        assertThatThrownBy(() -> loanService.loanBook(1L, 2L))
                .isInstanceOf(LoanNotAllowedException.class)
                .hasMessageContaining("maximum of 3");

        System.out.println("⚠️ Rejected loan – user reached max loan limit.");
    }

    @Test
    @DisplayName("🔁 Should extend loan if allowed")
    void shouldExtendLoan() throws Exception {
        loan.setExtensionCount(0);
        loan.setReturned(false);
        loan.setReturnDate(LocalDateTime.now().plusHours(2));
        book.getStatuses().clear();

        when(loanRepository.findById(3L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.extendLoan(3L);

        assertThat(result.getExtensionCount()).isEqualTo(1);
        assertThat(result.getReturnDate()).isAfter(LocalDateTime.now());

        System.out.println("⏳ Loan extended successfully.");
    }

    @Test
    @DisplayName("📤 Should return book and update status")
    void shouldReturnBook() throws Exception {
        loan.setReturned(false);

        when(loanRepository.findById(3L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.returnBook(3L);

        assertThat(result.getReturned()).isTrue();
        assertThat(result.getBook().getStatuses()).contains(BookStatus.AVAILABLE);

        System.out.println("📚 Book returned and status updated.");
    }
}
