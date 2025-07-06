package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kodilla.library.dto.LoanDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.LoanNotAllowedException;
import com.kodilla.library.exception.LoanNotFoundByIdException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.LoanMapper;
import com.kodilla.library.model.Loan;
import com.kodilla.library.service.LoanService;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @PostMapping
    public ResponseEntity<LoanDTO> loanBook(@RequestParam Long idUser, @RequestParam Long idBook)
            throws UserNotFoundByIdException, BookNotFoundByIdException, LoanNotAllowedException {
        Loan loan = loanService.loanBook(idUser, idBook);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    @PutMapping("/return/{idLoan}")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long idLoan)
            throws LoanNotFoundByIdException {
        Loan loan = loanService.returnBook(idLoan);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    @PutMapping("/extend/{idLoan}")
    public ResponseEntity<LoanDTO> extendLoan(@PathVariable Long idLoan)
            throws LoanNotFoundByIdException, LoanNotAllowedException {
        Loan loan = loanService.extendLoan(idLoan);
        return ResponseEntity.ok(loanMapper.toDto(loan));
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        List<Loan> loans = loanService.getLoansByUser(idUser);
        return ResponseEntity.ok(loanMapper.toDtoList(loans));
    }

    @GetMapping("/active")
    public ResponseEntity<List<LoanDTO>> getActiveLoans() {
        List<Loan> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loanMapper.toDtoList(loans));
    }
}
