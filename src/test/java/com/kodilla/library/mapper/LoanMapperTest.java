package com.kodilla.library.mapper;

import com.kodilla.library.dto.LoanDTO;
import com.kodilla.library.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LoanMapperTest {

    private LoanMapper mapper;
    private Loan loan;

    @BeforeEach
    void setUp() {
        mapper = new LoanMapper();

        User user = User.builder()
                .idUser(1L)
                .email("john@example.com")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("Effective Java")
                .statuses(new HashSet<>())
                .build();

        loan = Loan.builder()
                .idLoan(3L)
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.of(2024, 4, 1, 10, 0))
                .returnDate(LocalDateTime.of(2024, 4, 1, 16, 0))
                .returned(false)
                .extensionCount(0)
                .build();

        System.out.println("🔧 LoanMapper test initialized.");
    }

    @Test
    @DisplayName("📦 Should map Loan to LoanDTO")
    void shouldMapLoanToDto() {
        LoanDTO dto = mapper.toDto(loan);

        assertThat(dto).isNotNull();
        assertThat(dto.idLoan()).isEqualTo(3L);
        assertThat(dto.idUser()).isEqualTo(1L);
        assertThat(dto.idBook()).isEqualTo(2L);
        assertThat(dto.returned()).isFalse();
        assertThat(dto.extensionCount()).isEqualTo(0);

        System.out.println("✅ Loan mapped to LoanDTO successfully.");
    }

    @Test
    @DisplayName("📚 Should map list of Loans to list of LoanDTOs")
    void shouldMapLoanListToDtoList() {
        List<LoanDTO> result = mapper.toDtoList(List.of(loan));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idLoan()).isEqualTo(3L);

        System.out.println("📋 Loan list mapped to DTO list.");
    }
}
