package com.kodilla.library.mapper;

import com.kodilla.library.dto.FineDTO;
import com.kodilla.library.model.Fine;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import com.kodilla.library.model.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FineMapperTest {

    private FineMapper mapper;
    private Fine fine;

    @BeforeEach
    void setUp() {
        mapper = new FineMapper();

        User user = User.builder()
                .idUser(1L)
                .email("john@example.com")
                .build();

        Book book = Book.builder()
                .idBook(2L)
                .title("Clean Code")
                .build();

        Loan loan = Loan.builder()
                .idLoan(3L)
                .user(user)
                .book(book)
                .build();

        fine = Fine.builder()
                .idFine(4L)
                .loan(loan)
                .amount(new BigDecimal("12.50"))
                .issuedDate(LocalDateTime.of(2024, 4, 10, 12, 0))
                .paid(false)
                .build();

        System.out.println("🔧 FineMapper test initialized.");
    }

    @Test
    @DisplayName("💰 Should map Fine to FineDTO correctly")
    void shouldMapFineToDto() {
        FineDTO dto = mapper.toDto(fine);

        assertThat(dto).isNotNull();
        assertThat(dto.idFine()).isEqualTo(4L);
        assertThat(dto.idLoan()).isEqualTo(3L);
        assertThat(dto.amount()).isEqualTo(new BigDecimal("12.50"));
        assertThat(dto.issuedDate()).isEqualTo(LocalDateTime.of(2024, 4, 10, 12, 0));
        assertThat(dto.paid()).isFalse();

        System.out.println("✅ Mapped single Fine to DTO successfully.");
    }

    @Test
    @DisplayName("📋 Should map list of Fines to list of DTOs")
    void shouldMapFineListToDtoList() {
        List<FineDTO> result = mapper.toDtoList(List.of(fine));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idFine()).isEqualTo(4L);

        System.out.println("📋 Fine list mapped to DTO list.");
    }
}
