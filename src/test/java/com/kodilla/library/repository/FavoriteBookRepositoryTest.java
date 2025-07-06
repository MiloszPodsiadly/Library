package com.kodilla.library.repository;

import com.kodilla.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FavoriteBookRepositoryTest {

    @Autowired
    private FavoriteBookRepository favoriteBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash("hashed")
                .active(true)
                .build());

        book = bookRepository.save(Book.builder()
                .title("1984")
                .author("George Orwell")
                .isbn("1234567890")
                .available(true)
                .statuses(Set.of(BookStatus.AVAILABLE))
                .build());

        favoriteBookRepository.save(FavoriteBook.builder()
                .user(user)
                .book(book)
                .addedAt(LocalDateTime.now())
                .build());

        System.out.println("✅ Test setup completed: User and Book created.");
    }

    @Test
    @DisplayName("✅ Should check existence of favorite book")
    void shouldCheckIfFavoriteExists() {
        boolean exists = favoriteBookRepository.existsByUser_IdUserAndBook_IdBook(user.getIdUser(), book.getIdBook());

        assertThat(exists).isTrue();
        System.out.println("🔍 Favorite exists for userId=" + user.getIdUser() + " and bookId=" + book.getIdBook());
    }

    @Test
    @DisplayName("📚 Should retrieve favorites by user ID")
    void shouldFindAllByUserId() {
        List<FavoriteBook> favorites = favoriteBookRepository.findAllByUser_IdUser(user.getIdUser());

        assertThat(favorites).hasSize(1);
        System.out.println("📚 Found " + favorites.size() + " favorite(s) for userId=" + user.getIdUser());
    }

    @Test
    @DisplayName("🔍 Should find specific favorite by user and book ID")
    void shouldFindByUserAndBook() {
        Optional<FavoriteBook> favorite = favoriteBookRepository.findByUser_IdUserAndBook_IdBook(user.getIdUser(), book.getIdBook());

        assertThat(favorite).isPresent();
        System.out.println("🔎 Favorite found: " + favorite.get().getBook().getTitle());
    }

    @Test
    @DisplayName("🔢 Should count favorites for a book")
    void shouldCountFavoritesByBook() {
        Long count = favoriteBookRepository.countByBook_IdBook(book.getIdBook());

        assertThat(count).isEqualTo(1L);
        System.out.println("📈 Book id=" + book.getIdBook() + " has " + count + " favorite(s).");
    }
}
