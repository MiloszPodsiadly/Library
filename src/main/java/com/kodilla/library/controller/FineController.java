package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kodilla.library.dto.FineDTO;
import com.kodilla.library.exception.FineNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.FineMapper;
import com.kodilla.library.model.Fine;
import com.kodilla.library.service.FineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines")
public class FineController {

    private final FineService fineService;
    private final FineMapper fineMapper;

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<FineDTO>> getFinesByUser(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        List<Fine> fines = fineService.getFinesByUser(idUser);
        return ResponseEntity.ok(fineMapper.toDtoList(fines));
    }

    @PostMapping("/loan/{idLoan}")
    public ResponseEntity<FineDTO> addFineByLoan(
            @PathVariable Long idLoan,
            @RequestBody String reason
    ) {
        Fine fine = fineService.addFineForLoan(idLoan, reason);
        return ResponseEntity.ok(fineMapper.toDto(fine));
    }

    @PutMapping("/pay/{idFine}")
    public ResponseEntity<FineDTO> payFine(@PathVariable Long idFine)
            throws FineNotFoundException {
        Fine fine = fineService.payFine(idFine);
        return ResponseEntity.ok(fineMapper.toDto(fine));
    }
}
