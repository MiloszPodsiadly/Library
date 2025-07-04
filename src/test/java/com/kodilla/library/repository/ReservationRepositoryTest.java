package com.kodilla.library.repository;

import com.kodilla.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    private User user;
    private Book book;

    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder()
                .name("Res Tester")
                .email("res@test.com")
                .passwordHash("password")
                .active(true)
                .build());

        book = bookRepository.save(Book.builder()
                .title("Reservation Book")
                .author("Tester")
                .isbn("111-111-111")
                .available(false)
                .build());

        Reservation res1 = Reservation.builder()
                .user(user)
                .book(book)
                .active(true)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .reservationDate(LocalDateTime.now())
                .startDate(LocalDateTime.now().plusHours(1))
                .endDate(LocalDateTime.now().plusHours(7))
                .reservationOrder(1)
                .build();

        Reservation res2 = Reservation.builder()
                .user(user)
                .book(book)
                .active(false)
                .createdAt(LocalDateTime.now().minusDays(1))
                .reservationDate(LocalDateTime.now().minusDays(1))
                .startDate(LocalDateTime.now().minusHours(6))
                .endDate(LocalDateTime.now().minusHours(1))
                .reservationOrder(2)
                .build();

        reservationRepository.saveAll(List.of(res1, res2));
        System.out.println("🧪 Setup complete: 2 reservations saved (1 active, 1 inactive)");
    }

    @Test
    @DisplayName("👤 Should find all reservations by user ID")
    void shouldFindAllByUserId() {
        List<Reservation> result = reservationRepository.findAllByUser_IdUser(user.getIdUser());

        assertThat(result).hasSize(2);
        System.out.println("👤 Reservations for user: " + result.size());
    }

    @Test
    @DisplayName("📚 Should count reservations for a book")
    void shouldCountByBookId() {
        Long count = reservationRepository.countByBook_IdBook(book.getIdBook());

        assertThat(count).isEqualTo(2);
        System.out.println("📚 Total reservations for book: " + count);
    }

    @Test
    @DisplayName("📖 Should find all reservations for a book")
    void shouldFindAllByBookId() {
        List<Reservation> result = reservationRepository.findAllByBook_IdBook(book.getIdBook());

        assertThat(result).hasSize(2);
        System.out.println("📖 Found " + result.size() + " reservation(s) for book.");
    }

    @Test
    @DisplayName("✅ Should detect if user has active reservation")
    void shouldCheckIfUserHasActiveReservation() {
        boolean exists = reservationRepository
                .existsByUser_IdUserAndBook_IdBookAndActiveTrue(user.getIdUser(), book.getIdBook());

        assertThat(exists).isTrue();
        System.out.println("✅ User has an active reservation: " + exists);
    }

    @Test
    @DisplayName("🔥 Should return only active reservations")
    void shouldReturnActiveReservations() {
        List<Reservation> actives = reservationRepository.findAllByActiveTrue();

        assertThat(actives).hasSize(1);
        assertThat(actives.get(0).getActive()).isTrue();
        System.out.println("🔥 Found " + actives.size() + " active reservation(s).");
    }
}
