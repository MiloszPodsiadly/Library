package com.kodilla.library.security;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.kodilla.library.model.User;
@Component
public class AccessGuard {
    public boolean checkOwner(Long idUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new SecurityException("No authentication");
        }

        if (user.getTokenExpiresAt() == null || user.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new SecurityException("Token has expired please generate token again");
        }
        return user.getIdUser().equals(idUser);
    }
}
