package com.kodilla.library.service;

import com.kodilla.library.exception.FineNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Fine;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.FineRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.UserRepository;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @Mock private FineRepository fineRepository;

    @Mock private UserRepository userRepository;

    @Mock private LoanRepository loanRepository;

    @InjectMocks private FineService fineService;

    private User user;
    private Fine unpaidFine;

    @BeforeAll
    static void beforeAll() {
        System.out.println("\n🚀 Starting tests for 📘 FineService...");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().idUser(1L).name("Alice").build();

        unpaidFine = Fine.builder()
                .idFine(1L)
                .user(user)
                .reason("Damaged book")
                .issuedDate(LocalDateTime.now().minusDays(5))
                .amount(BigDecimal.ZERO)
                .paid(false)
                .build();

        System.out.println("🧪 Initialized mocks for FineService test.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ Test completed.\n");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("🎉 All FineService tests finished.");
    }

    @Test
    @DisplayName("📄 Should return fines for valid user and calculate updated amounts")
    void shouldReturnFinesForUser() throws UserNotFoundByIdException {
        unpaidFine.setIssuedDate(LocalDateTime.now().minusDays(3));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(fineRepository.findAllByUser_IdUser(1L)).thenReturn(List.of(unpaidFine));

        List<Fine> result = fineService.getFinesByUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isGreaterThan(BigDecimal.ZERO);
        System.out.println("📘 Fine amount after update: " + result.get(0).getAmount());
    }


    @Test
    @DisplayName("❌ Should throw if user not found when fetching fines")
    void shouldThrowWhenUserNotFound_getFines() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> fineService.getFinesByUser(99L))
                .isInstanceOf(UserNotFoundByIdException.class)
                .hasMessageContaining("99");

        System.out.println("❗ Expected exception for unknown user.");
    }
    @Test
    @DisplayName("➕ Should add fine for existing user")
    void shouldAddFineForLoan() {
        Loan loan = Loan.builder()
                .idLoan(1L)
                .user(user)
                .returned(false)
                .build();

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(fineRepository.existsByLoan_IdLoan(1L)).thenReturn(false);
        when(fineRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String reason = "Late return";

        Fine fine = fineService.addFineForLoan(1L, reason);

        assertThat(fine).isNotNull();
        assertThat(fine.getReason()).isEqualTo(reason);
        assertThat(fine.getUser()).isEqualTo(user);
        assertThat(fine.getPaid()).isFalse();
        assertThat(fine.getAmount()).isEqualByComparingTo(new BigDecimal("0.10"));

        System.out.println("✅ Fine added successfully for loan.");
    }

    @Test
    @DisplayName("❌ Should throw when loan not found when adding fine")
    void shouldThrowWhenLoanNotFound_addFine() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fineService.addFineForLoan(1L, "Late return"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Loan with ID 1 not found");

        System.out.println("❗ Exception correctly thrown when adding fine to nonexistent loan.");
    }

    @Test
    @DisplayName("💰 Should mark fine as paid and calculate amount")
    void shouldPayFine() throws FineNotFoundException {
        unpaidFine.setIssuedDate(LocalDateTime.now().minusDays(3));
        unpaidFine.setPaid(false);

        when(fineRepository.findById(1L)).thenReturn(Optional.of(unpaidFine));
        when(fineRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Fine result = fineService.payFine(1L);

        assertThat(result.getPaid()).isTrue();
        assertThat(result.getAmount()).isEqualByComparingTo("0.30");
        System.out.println("💸 Fine paid, amount was: " + result.getAmount());
    }

    @Test
    @DisplayName("❌ Should throw when fine not found while paying")
    void shouldThrowWhenFineNotFound_payFine() {
        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fineService.payFine(1L))
                .isInstanceOf(FineNotFoundException.class)
                .hasMessageContaining("1");

        System.out.println("❗ Exception correctly thrown when paying nonexistent fine.");
    }
}
