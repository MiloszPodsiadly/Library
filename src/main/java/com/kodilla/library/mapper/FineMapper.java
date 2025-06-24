package com.kodilla.library.mapper;

import com.kodilla.library.dto.FineDTO;
import com.kodilla.library.model.Fine;
import com.kodilla.library.model.Loan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FineMapper {

    public FineDTO toDto(Fine fine) {
        if (fine == null) {
            return null;
        }
        return new FineDTO(
                fine.getIdFine(),
                fine.getLoan().getIdLoan(),
                fine.getAmount(),
                fine.getIssuedDate(),
                fine.getPaid()
        );
    }

    public Fine toEntity(FineDTO dto, Loan loan) {
        if (dto == null) {
            return null;
        }
        return Fine.builder()
                .idFine(dto.idFine())
                .loan(loan)
                .amount(dto.amount())
                .issuedDate(dto.issuedDate())
                .paid(dto.paid())
                .build();
    }

    public List<FineDTO> toDtoList(List<Fine> fines) {
        return fines.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
