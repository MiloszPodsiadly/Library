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

class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private ReviewService reviewService;

    private User user;
    private Book book;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().idUser(1L).name("Alice").build();
        book = Book.builder().idBook(2L).title("Effective Java").build();
        review = Review.builder()
                .idReview(10L)
                .user(user)
                .book(book)
                .rating(5)
                .comment("Excellent book")
                .createdAt(LocalDateTime.now())
                .build();

        System.out.println("🔧 Mocks initialized for ReviewService.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ ReviewService test completed.\n");
    }

    @Test
    @DisplayName("📝 Should add review successfully")
    void shouldAddReview() throws Exception {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.addReview(1L, 2L, "Awesome!", 4);

        assertThat(result.getBook().getTitle()).isEqualTo("Effective Java");
        assertThat(result.getRating()).isEqualTo(4);
        System.out.println("📝 Review added with rating: " + result.getRating());
    }

    @Test
    @DisplayName("❌ Should throw when book not found during review add")
    void shouldThrowWhenBookNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> reviewService.addReview(1L, 2L, "Cool", 3))
                .isInstanceOf(BookNotFoundByIdException.class);
        System.out.println("❌ Book not found for review.");
    }

    @Test
    @DisplayName("❌ Should throw when user not found during review add")
    void shouldThrowWhenUserNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.addReview(1L, 2L, "Great!", 5))
                .isInstanceOf(UserNotFoundByIdException.class);
        System.out.println("❌ User not found for review.");
    }

    @Test
    @DisplayName("📚 Should return reviews for book")
    void shouldReturnReviewsForBook() throws BookNotFoundByIdException {
        when(bookRepository.existsById(2L)).thenReturn(true);
        when(reviewRepository.findByBook_IdBook(2L)).thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsForBook(2L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getComment()).contains("Excellent");
        System.out.println("📚 Reviews found for book: " + result.size());
    }

    @Test
    @DisplayName("❌ Should throw when book not found while fetching reviews")
    void shouldThrowWhenBookNotFoundFetchingReviews() {
        when(bookRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.getReviewsForBook(2L))
                .isInstanceOf(BookNotFoundByIdException.class);
        System.out.println("❌ Book not found when fetching reviews.");
    }

    @Test
    @DisplayName("👤 Should return reviews by user")
    void shouldReturnUserReviews() throws UserNotFoundByIdException {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByUser_IdUser(1L)).thenReturn(List.of(review));

        List<Review> result = reviewService.getUserReviews(1L);

        assertThat(result).hasSize(1);
        System.out.println("👤 Reviews found for user: " + result.size());
    }

    @Test
    @DisplayName("❌ Should throw when user not found while fetching their reviews")
    void shouldThrowWhenUserNotFoundFetchingReviews() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.getUserReviews(1L))
                .isInstanceOf(UserNotFoundByIdException.class);
        System.out.println("❌ User not found when fetching reviews.");
    }

    @Test
    @DisplayName("🗑️ Should delete review if exists")
    void shouldDeleteReview() throws ReviewNotFoundByIdException {
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(10L);

        verify(reviewRepository).delete(review);
        System.out.println("🗑️ Review deleted successfully.");
    }

    @Test
    @DisplayName("❌ Should throw when review to delete not found")
    void shouldThrowWhenReviewNotFoundToDelete() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.deleteReview(99L))
                .isInstanceOf(ReviewNotFoundByIdException.class);
        System.out.println("❌ Review not found for deletion.");
    }
}
