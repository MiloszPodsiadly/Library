package com.kodilla.library;


import com.kodilla.library.dto.LoanDto;
import com.kodilla.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.kodilla.library.controller.LoanController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerTestLoan {

    private LoanService loanService;
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        loanService = mock(LoanService.class); // Mock serwisu
        loanController = new LoanController(loanService); // Tworzenie instancji kontrolera
    }

    @Test
    void shouldCreateLoan() {
        // Given
        Long userId = 1L;
        Long bookId = 2L;
        int loanDays = 30;

        LoanDto loanDtoMock = new LoanDto(); // Przykładowe LoanDto
        when(loanService.createLoan(any(), eq(bookId), eq(loanDays))).thenReturn(loanDtoMock);

        // When
        ResponseEntity<LoanDto> response = loanController.createLoan(userId, bookId, loanDays);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(loanDtoMock, response.getBody());
        verify(loanService, times(1)).createLoan(any(), eq(bookId), eq(loanDays));
    }

    @Test
    void shouldExtendLoan() {
        // Given
        Long loanId = 1L;
        int extraDays = 15;

        LoanDto loanDtoMock = new LoanDto(); // Przykładowe LoanDto
        when(loanService.extendLoan(loanId, extraDays)).thenReturn(loanDtoMock);

        // When
        ResponseEntity<LoanDto> response = loanController.extendLoan(loanId, extraDays);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loanDtoMock, response.getBody());
        verify(loanService, times(1)).extendLoan(loanId, extraDays);
    }
}