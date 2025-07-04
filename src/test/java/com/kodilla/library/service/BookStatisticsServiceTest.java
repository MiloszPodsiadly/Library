package com.kodilla.library.service;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatistics;
import com.kodilla.library.repository.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookStatisticsServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private LoanRepository loanRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private FavoriteBookRepository favoriteBookRepository;
    @Mock private BookStatisticsRepository bookStatisticsRepository;

    @InjectMocks
    private BookStatisticsService service;

    private Book book1, book2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book1 = Book.builder().idBook(1L).title("Book One").build();
        book2 = Book.builder().idBook(2L).title("Book Two").build();

        System.out.println("🧪 Initialized mocks and service");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ Test completed.\n");
    }

    @Test
    @DisplayName("📊 Should return books sorted by loan count descending")
    void shouldReturnBooksSortedByLoanCount() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(loanRepository.countByBook_IdBook(1L)).thenReturn(5L);
        when(loanRepository.countByBook_IdBook(2L)).thenReturn(10L);

        // When
        List<Book> result = service.getAllBooksSortedByLoanCountDesc();

        // Then
        assertThat(result).containsExactly(book2, book1);
    }

    @Test
    @DisplayName("📚 Should return top 10 most loaned books")
    void shouldReturnTop10MostLoanedBooks() {
        // Given
        List<Book> books = new ArrayList<>();
        for (long i = 1; i <= 15; i++) {
            books.add(Book.builder().idBook(i).title("Book " + i).build());
            when(loanRepository.countByBook_IdBook(i)).thenReturn(15 - i); // decreasing count
        }

        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = service.getTop10MostLoanedBooks();

        // Then
        assertThat(result).hasSize(10);
        assertThat(result.get(0).getIdBook()).isEqualTo(1L); // Highest loan count
    }

    @Test
    @DisplayName("📖 Should return top 3 reserved books")
    void shouldReturnTop3ReservedBooks() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(reservationRepository.countByBook_IdBook(1L)).thenReturn(2L);
        when(reservationRepository.countByBook_IdBook(2L)).thenReturn(5L);

        // When
        List<Book> result = service.getTop3ReservedBooks();

        // Then
        assertThat(result).containsExactly(book2, book1);
    }

    @Test
    @DisplayName("❤️ Should return top 3 favorite books")
    void shouldReturnTop3FavoriteBooks() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(favoriteBookRepository.countByBook_IdBook(1L)).thenReturn(1L);
        when(favoriteBookRepository.countByBook_IdBook(2L)).thenReturn(3L);

        // When
        List<Book> result = service.getTop3FavoriteBooks();

        // Then
        assertThat(result).containsExactly(book2, book1);
    }

    @Test
    @DisplayName("📈 Should return loan count for specific book")
    void shouldReturnLoanCountForBook() {
        // Given
        when(loanRepository.countByBook_IdBook(1L)).thenReturn(42L);

        // When
        Long count = service.getLoanCountForBook(1L);

        // Then
        assertThat(count).isEqualTo(42L);
    }

    @Test
    @DisplayName("🔢 Should return reservation count for book")
    void shouldReturnReservationCountForBook() {
        // Given
        when(reservationRepository.countByBook_IdBook(1L)).thenReturn(7L);

        // When
        Long count = service.getReservationCountForBook(1L);

        // Then
        assertThat(count).isEqualTo(7L);
    }

    @Test
    @DisplayName("⭐ Should return average rating for a book")
    void shouldReturnAverageRating() {
        // Given
        when(reviewRepository.findAverageRatingByBookId(1L)).thenReturn(4.2);

        // When
        Double avg = service.getAverageRatingForBook(1L);

        // Then
        assertThat(avg).isEqualTo(4.2);
    }

    @Test
    @DisplayName("💗 Should return favorite count in formatted string")
    void shouldReturnFormattedFavoriteCount() {
        // Given
        when(favoriteBookRepository.countByBook_IdBook(1L)).thenReturn(9L);

        // When
        String result = service.getFavoriteCountForBook(1L);

        // Then
        assertThat(result).isEqualTo("How many likes it has: 9");
    }

    @Test
    @DisplayName("📘 Should return book by id or throw if not found")
    void shouldReturnBookByIdOrThrow() throws BookNotFoundByIdException {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        // When
        Book found = service.getBookById(1L);

        // Then
        assertThat(found.getTitle()).isEqualTo("Book One");
    }

    @Test
    @DisplayName("❌ Should throw when book not found")
    void shouldThrowWhenBookNotFound() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> service.getBookById(99L))
                .isInstanceOf(BookNotFoundByIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("📄 Should return book statistics if exists")
    void shouldReturnBookStatistics() {
        // Given
        BookStatistics stats = new BookStatistics();
        when(bookStatisticsRepository.findByBook_IdBook(1L)).thenReturn(Optional.of(stats));

        // When
        BookStatistics result = service.getStatisticsByBookId(1L);

        // Then
        assertThat(result).isEqualTo(stats);
    }

    @Test
    @DisplayName("📄 Should return null if book statistics not found")
    void shouldReturnNullWhenStatisticsNotFound() {
        // Given
        when(bookStatisticsRepository.findByBook_IdBook(1L)).thenReturn(Optional.empty());

        // When
        BookStatistics result = service.getStatisticsByBookId(1L);

        // Then
        assertThat(result).isNull();
    }
}
