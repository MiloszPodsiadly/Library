package com.kodilla.library.controller;

import com.kodilla.library.dto.FineDTO;
import com.kodilla.library.exception.FineNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.FineMapper;
import com.kodilla.library.model.Fine;
import com.kodilla.library.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines")
public class FineController {

    private final FineService fineService;
    private final FineMapper fineMapper;

    // ✅ GET /fines/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FineDTO>> getFinesByUser(@PathVariable Long userId)
            throws UserNotFoundByIdException {
        List<Fine> fines = fineService.getFinesByUser(userId);
        return ResponseEntity.ok(fineMapper.toDtoList(fines));
    }

    // ✅ POST /fines?userId=...&reason=...
    @PostMapping
    public ResponseEntity<FineDTO> addFine(
            @RequestParam Long userId,
            @RequestParam String reason
    ) throws UserNotFoundByIdException {
        Fine fine = fineService.addFine(userId, reason);
        return ResponseEntity.ok(fineMapper.toDto(fine));
    }

    // ✅ PUT /fines/{fineId}/pay
    @PutMapping("/{fineId}/pay")
    public ResponseEntity<FineDTO> payFine(@PathVariable Long fineId)
            throws FineNotFoundException {
        Fine fine = fineService.payFine(fineId);
        return ResponseEntity.ok(fineMapper.toDto(fine));
    }
}
