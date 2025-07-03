package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.LoanDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;

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

    public Loan toEntity(LoanDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }
        return Loan.builder()
                .idLoan(dto.idLoan())
                .user(user)
                .book(book)
                .loanDate(dto.loanDate())
                .returnDate(dto.returnDate())
                .returned(dto.returned())
                .extensionCount(dto.extensionCount())
                .build();
    }

    public List<LoanDTO> toDtoList(List<Loan> loans) {
        return loans.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
