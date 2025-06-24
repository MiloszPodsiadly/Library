package com.kodilla.library.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewDTO(
        Long idReview,
        Long idUser,
        Long idBook,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {}

