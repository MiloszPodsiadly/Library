package com.kodilla.library.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record FineDTO(
        Long idFine,
        Long idLoan,
        BigDecimal amount,
        LocalDateTime issuedDate,
        Boolean paid,
        String reason
) {}
