package com.kodilla.library.service;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = Book.builder()
                .idBook(1L)
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("1234567890")
                .available(true)
                .statuses(new HashSet<>())
                .build();
        System.out.println("🔧 Mocked BookService setup completed.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✔️ Test completed.\n");
    }

    @Test
    @DisplayName("📚 Should return all books from repository")
    void shouldReturnAllBooks() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Effective Java");
    }

    @Test
    @DisplayName("🔎 Should return book by ID")
    void shouldReturnBookById() throws BookNotFoundByIdException {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        // When
        Book result = bookService.getBookById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo("Joshua Bloch");
    }

    @Test
    @DisplayName("⚠️ Should throw exception when book not found")
    void shouldThrowWhenBookNotFoundById() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundByIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("➕ Should add new book to repository")
    void shouldAddNewBook() {
        // Given
        when(bookRepository.save(sampleBook)).thenReturn(sampleBook);

        // When
        Book result = bookService.addBook(sampleBook);

        // Then
        assertThat(result.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("🗑️ Should delete existing book by ID")
    void shouldDeleteBook() throws BookNotFoundByIdException {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        doNothing().when(bookRepository).delete(sampleBook);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository, times(1)).delete(sampleBook);
    }

    @Test
    @DisplayName("🔍 Should search books by title (case-insensitive)")
    void shouldSearchByTitle() {
        // Given
        when(bookRepository.findByTitleContainingIgnoreCase("java"))
                .thenReturn(List.of(sampleBook));

        // When
        List<Book> result = bookService.searchByTitle("java");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).containsIgnoringCase("java");
    }

    @Test
    @DisplayName("👨‍💻 Should search books by author")
    void shouldSearchByAuthor() {
        // Given
        when(bookRepository.findByAuthorContainingIgnoreCase("bloch"))
                .thenReturn(List.of(sampleBook));

        // When
        List<Book> result = bookService.searchByAuthor("bloch");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).containsIgnoringCase("bloch");
    }

    @Test
    @DisplayName("✏️ Should update existing book with new data")
    void shouldUpdateBook() throws BookNotFoundByIdException {
        // Given
        Book updated = Book.builder()
                .idBook(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9876543210")
                .available(false)
                .statuses(new HashSet<>())
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Book result = bookService.updateBook(1L, updated);

        // Then
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.getAvailable()).isFalse();
        assertThat(result.getIsbn()).isEqualTo("9876543210");
    }
}
