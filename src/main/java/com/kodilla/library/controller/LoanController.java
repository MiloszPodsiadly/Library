package com.kodilla.library.controller;

import com.kodilla.library.dto.LoanDTO;
import com.kodilla.library.exception.*;
import com.kodilla.library.mapper.LoanMapper;
import com.kodilla.library.model.Loan;
import com.kodilla.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    // ✅ POST /loans?userId=&bookId=
    @PostMapping
    public ResponseEntity<LoanDTO> loanBook(@RequestParam Long userId, @RequestParam Long bookId)
            throws UserNotFoundByIdException, BookNotFoundByIdException, LoanNotAllowedException {
        Loan loan = loanService.loanBook(userId, bookId);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    // ✅ PUT /loans/{loanId}/return
    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long loanId)
            throws LoanNotFoundByIdException {
        Loan loan = loanService.returnBook(loanId);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    // ✅ PUT /loans/{loanId}/extend
    @PutMapping("/{loanId}/extend")
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long loanId)
            throws LoanNotFoundByIdException, LoanNotAllowedException {
        Loan loan = loanService.extendLoan(loanId);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    // ✅ GET /loans/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable Long userId)
            throws UserNotFoundByIdException {
        List<Loan> loans = loanService.getLoansByUser(userId);
        return ResponseEntity.ok(loanMapper.toDtoList(loans));
    }

    // ✅ GET /loans/active
    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<Loan> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loanMapper.toDtoList(loans));
    }
}
