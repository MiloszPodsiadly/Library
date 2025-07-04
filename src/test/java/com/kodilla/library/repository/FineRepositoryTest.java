package com.kodilla.library.repository;

import com.kodilla.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FineRepositoryTest {

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("Fine Tester")
                .email("fine@test.com")
                .passwordHash("secret")
                .active(true)
                .build());

        book = bookRepository.save(Book.builder()
                .title("Java Book")
                .author("Expert Dev")
                .isbn("123-456-789")
                .available(true)
                .build());

        loan = loanRepository.save(Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.now().minusDays(10))
                .returnDate(null)
                .returned(false)
                .build());

        Fine fine = Fine.builder()
                .loan(loan)
                .amount(BigDecimal.valueOf(25))
                .reason("Late return")
                .issuedDate(LocalDateTime.now())
                .paid(false)
                .user(user)
                .build();

        fineRepository.save(fine);

        System.out.println("✅ Test setup completed: Fine created for user " + user.getEmail());
    }

    @Test
    @DisplayName("💸 Should return all fines for a user")
    void shouldFindAllFinesByUserId() {
        List<Fine> fines = fineRepository.findAllByUser_IdUser(user.getIdUser());

        assertThat(fines).hasSize(1);
        assertThat(fines.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(25));
        System.out.println("💳 Found " + fines.size() + " fine(s) for user: " + user.getEmail());
    }

    @Test
    @DisplayName("📉 Should return empty list if user has no fines")
    void shouldReturnEmptyListForUserWithoutFines() {
        User anotherUser = userRepository.save(User.builder()
                .name("No Fine User")
                .email("nofines@test.com")
                .passwordHash("none")
                .active(true)
                .build());

        List<Fine> fines = fineRepository.findAllByUser_IdUser(anotherUser.getIdUser());

        assertThat(fines).isEmpty();
        System.out.println("👌 No fines found for user: " + anotherUser.getEmail());
    }
}
