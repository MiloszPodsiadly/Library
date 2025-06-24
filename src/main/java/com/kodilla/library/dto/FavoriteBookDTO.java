package com.kodilla.library.dto;
import java.time.LocalDateTime;
public record FavoriteBookDTO(
        Long idFavoriteBook,
        Long idUser,
        Long idBook,
        LocalDateTime addedAt
) {}

