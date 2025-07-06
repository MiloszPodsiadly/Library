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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class LoanRepositoryTest {

    @Autowired private LoanRepository loanRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    private User user;
    private Book book;

    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder()
                .name("Loan Tester")
                .email("loan@test.com")
                .passwordHash("secret")
                .active(true)
                .build());

        book = bookRepository.save(Book.builder()
                .title("Test Book")
                .author("Author")
                .isbn("987-654-321")
                .available(true)
                .build());

        Loan loan1 = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.now().minusDays(5))
                .returned(false)
                .build();

        Loan loan2 = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.now().minusDays(10))
                .returnDate(LocalDateTime.now().minusDays(2))
                .returned(true)
                .build();

        loanRepository.save(loan1);
        loanRepository.save(loan2);

        System.out.println("📚 Setup complete: 2 loans created (1 active, 1 returned)");
    }

    @Test
    @DisplayName("🔍 Should find all loans for a user")
    void shouldFindLoansByUserId() {
        List<Loan> loans = loanRepository.findByUser_IdUser(user.getIdUser());

        assertThat(loans).hasSize(2);
        System.out.println("🔎 Found " + loans.size() + " loan(s) for user: " + user.getEmail());
    }

    @Test
    @DisplayName("⏳ Should find all unreturned loans")
    void shouldFindUnreturnedLoans() {
        List<Loan> unreturned = loanRepository.findByReturnedFalse();

        assertThat(unreturned).hasSize(1);
        assertThat(unreturned.get(0).getReturned()).isFalse();
        System.out.println("📌 Unreturned loans count: " + unreturned.size());
    }

    @Test
    @DisplayName("📈 Should count unreturned loans by user")
    void shouldCountUnreturnedLoansByUser() {
        long count = loanRepository.countByUser_IdUserAndReturnedFalse(user.getIdUser());

        assertThat(count).isEqualTo(1);
        System.out.println("📊 Unreturned loans for user: " + count);
    }

    @Test
    @DisplayName("📚 Should count total loans for book")
    void shouldCountLoansByBook() {
        long count = loanRepository.countByBook_IdBook(book.getIdBook());

        assertThat(count).isEqualTo(2);
        System.out.println("📕 Total loans for book '" + book.getTitle() + "': " + count);
    }
}
