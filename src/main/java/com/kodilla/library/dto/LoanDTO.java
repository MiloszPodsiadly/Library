package com.kodilla.library.dto;

import java.time.LocalDateTime;

public record LoanDTO(
        Long idLoan,
        Long idUser,
        Long idBook,
        LocalDateTime loanDate,
        LocalDateTime returnDate,
        Boolean returned,
        Integer extensionCount
) {}

