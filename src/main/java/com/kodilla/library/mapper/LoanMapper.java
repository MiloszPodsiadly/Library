package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.LoanDTO;
import com.kodilla.library.model.Loan;

@Component
public class LoanMapper {

    public LoanDTO toDto(Loan loan) {
        if (loan == null) {
            return null;
        }
        return new LoanDTO(
                loan.getIdLoan(),
                loan.getUser().getIdUser(),
                loan.getBook().getIdBook(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getReturned(),
                loan.getExtensionCount()
        );
    }

    public List<LoanDTO> toDtoList(List<Loan> loans) {
        return loans.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
