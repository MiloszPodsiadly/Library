package com.kodilla.library.dto;

public record BookStatisticsDTO(
        Long idBookStatistics,
        Long loanCount,
        Long reservationCount,
        Double averageRating,
        Long favoriteCount
) {}

