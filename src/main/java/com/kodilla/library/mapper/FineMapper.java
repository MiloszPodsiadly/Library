package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.FineDTO;
import com.kodilla.library.model.Fine;

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

    public List<FineDTO> toDtoList(List<Fine> fines) {
        return fines.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
