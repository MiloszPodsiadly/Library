package com.kodilla.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kodilla.library.dto.LoanDto;
import com.kodilla.library.model.User;
import com.kodilla.library.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/create")
    public ResponseEntity<LoanDto> createLoan(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int loanDays) {
        User user = new User(); // Pobierz użytkownika po ID
        LoanDto loanDto = loanService.createLoan(user, bookId, loanDays);
        return new ResponseEntity<>(loanDto, HttpStatus.CREATED);
    }

    @PostMapping("/extend/{loanId}")
    public ResponseEntity<LoanDto> extendLoan(@PathVariable Long loanId, @RequestParam int extraDays) {
        LoanDto loanDto = loanService.extendLoan(loanId, extraDays); // Metoda zwraca LoanDto
        return new ResponseEntity<>(loanDto, HttpStatus.OK);
    }
}
