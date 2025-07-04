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

class FavoriteBookServiceTest {

    @Mock private FavoriteBookRepository favoriteBookRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;

    @InjectMocks
    private FavoriteBookService service;

    private User user;
    private Book book;
    private FavoriteBook favorite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().idUser(1L).name("Alice").build();
        book = Book.builder().idBook(2L).title("1984").build();
        favorite = FavoriteBook.builder()
                .user(user)
                .book(book)
                .addedAt(LocalDateTime.now())
                .build();

        System.out.println("🧪 Mocks initialized for FavoriteBookService.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ FavoriteBookService test completed.\n");
    }

    @Test
    @DisplayName("📄 Should return list of favorites for valid user")
    void shouldReturnFavoritesByUser() throws UserNotFoundByIdException {
        // given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(favoriteBookRepository.findAllByUser_IdUser(1L)).thenReturn(List.of(favorite));

        // when
        List<FavoriteBook> result = service.getFavoritesByUser(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBook().getTitle()).isEqualTo("1984");
    }

    @Test
    @DisplayName("❌ Should throw when user not found while getting favorites")
    void shouldThrowWhenUserNotFound_getFavorites() {
        // given
        when(userRepository.existsById(99L)).thenReturn(false);

        // then
        assertThatThrownBy(() -> service.getFavoritesByUser(99L))
                .isInstanceOf(UserNotFoundByIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("➕ Should add favorite when valid user and book provided")
    void shouldAddFavoriteBook() throws Exception {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(favoriteBookRepository.existsByUser_IdUserAndBook_IdBook(1L, 2L)).thenReturn(false);
        when(favoriteBookRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        FavoriteBook result = service.addFavoriteBook(1L, 2L);

        // then
        assertThat(result.getUser().getName()).isEqualTo("Alice");
        assertThat(result.getBook().getTitle()).isEqualTo("1984");
    }

    @Test
    @DisplayName("❗ Should throw if favorite already exists")
    void shouldThrowWhenFavoriteAlreadyExists() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(favoriteBookRepository.existsByUser_IdUserAndBook_IdBook(1L, 2L)).thenReturn(true);

        // then
        assertThatThrownBy(() -> service.addFavoriteBook(1L, 2L))
                .isInstanceOf(FavoriteAlreadyExistsException.class)
                .hasMessageContaining("This user has this book in his/her favorites list");
    }

    @Test
    @DisplayName("🗑️ Should remove favorite book if exists")
    void shouldRemoveFavoriteBook() throws FavoriteNotFoundException {
        // given
        when(favoriteBookRepository.findByUser_IdUserAndBook_IdBook(1L, 2L))
                .thenReturn(Optional.of(favorite));

        // when
        service.removeFavoriteBook(1L, 2L);

        // then
        verify(favoriteBookRepository).delete(favorite);
    }

    @Test
    @DisplayName("🚫 Should throw when trying to remove nonexistent favorite")
    void shouldThrowWhenRemovingNonexistentFavorite() {
        // given
        when(favoriteBookRepository.findByUser_IdUserAndBook_IdBook(1L, 2L))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.removeFavoriteBook(1L, 2L))
                .isInstanceOf(FavoriteNotFoundException.class)
                .hasMessageContaining("This user doesn't have this book in his/her favorites list");
    }

    @Test
    @DisplayName("❌ Should throw when user or book not found while adding")
    void shouldThrowWhenUserOrBookNotFound() {
        // User not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFavoriteBook(1L, 2L))
                .isInstanceOf(UserNotFoundByIdException.class)
                .hasMessageContaining("1");

        // Book not found
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFavoriteBook(1L, 2L))
                .isInstanceOf(BookNotFoundByIdException.class)
                .hasMessageContaining("2");
    }
}
