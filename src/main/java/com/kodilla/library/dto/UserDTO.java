package com.kodilla.library.dto;
import java.time.LocalDateTime;
public record UserDTO(
        String name,
        String email,
        Boolean active,
        String token,
        LocalDateTime tokenCreatedAt,
        LocalDateTime tokenExpiresAt,
        String rawPassword // ✅ DODANE
) {}


