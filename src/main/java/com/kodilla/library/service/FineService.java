package com.kodilla.library.service;

import com.kodilla.library.exception.FineNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Fine;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.FineRepository;
import com.kodilla.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {

    private static final BigDecimal DAILY_RATE = new BigDecimal("0.10");

    private final FineRepository fineRepository;
    private final UserRepository userRepository;

    public List<Fine> getFinesByUser(Long idUser) throws UserNotFoundByIdException {
        if (!userRepository.existsById(idUser)) {
            throw new UserNotFoundByIdException(idUser);
        }

        List<Fine> fines = fineRepository.findAllByUser_IdUser(idUser);

        return fines.stream()
                .map(this::calculateFineAmount)
                .collect(Collectors.toList());
    }

    public Fine addFine(Long idUser, String reason) throws UserNotFoundByIdException {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Fine fine = Fine.builder()
                .user(user)
                .reason(reason)
                .issuedDate(LocalDateTime.now())
                .amount(BigDecimal.ZERO)
                .paid(false)
                .build();

        return fineRepository.save(fine);
    }

    public Fine payFine(Long idFine) throws FineNotFoundException {
        Fine fine = fineRepository.findById(idFine)
                .orElseThrow(() -> new FineNotFoundException(idFine));

        fine.setAmount(calculateAmountUntilNow(fine));
        fine.setPaid(true);
        return fineRepository.save(fine);
    }

    private Fine calculateFineAmount(Fine fine) {
        if (!fine.getPaid()) {
            BigDecimal updated = calculateAmountUntilNow(fine);
            fine.setAmount(updated);
        }
        return fine;
    }

    private BigDecimal calculateAmountUntilNow(Fine fine) {
        long days = ChronoUnit.DAYS.between(fine.getIssuedDate(), LocalDate.now());
        if (days < 0) days = 0;
        return DAILY_RATE.multiply(BigDecimal.valueOf(days));
    }
}
