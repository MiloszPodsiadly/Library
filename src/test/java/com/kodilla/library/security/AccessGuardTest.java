package com.kodilla.library.security;

import com.kodilla.library.model.User;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AccessGuardTest {

    private final AccessGuard accessGuard = new AccessGuard();

    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("✅ Should return true if user is owner and token is valid")
    void shouldReturnTrueForValidUser() {
        User user = User.builder()
                .idUser(1L)
                .tokenExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        var auth = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean result = accessGuard.checkOwner(1L);

        assertThat(result).isTrue();
        System.out.println("🔐 Access granted: user matches and token valid.");
    }

    @Test
    @DisplayName("🚫 Should throw if authentication is null")
    void shouldThrowIfAuthenticationIsNull() {
        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> accessGuard.checkOwner(1L))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("No authentication");

        System.out.println("❗ Blocked: no authentication.");
    }

    @Test
    @DisplayName("🚫 Should throw if principal is not a User instance")
    void shouldThrowIfPrincipalIsInvalid() {
        var auth = new UsernamePasswordAuthenticationToken("notAUser", null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThatThrownBy(() -> accessGuard.checkOwner(1L))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("No authentication");

        System.out.println("❗ Blocked: invalid principal.");
    }

    @Test
    @DisplayName("⌛ Should throw if token is expired")
    void shouldThrowIfTokenExpired() {
        User user = User.builder()
                .idUser(1L)
                .tokenExpiresAt(LocalDateTime.now().minusMinutes(1))
                .build();

        var auth = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThatThrownBy(() -> accessGuard.checkOwner(1L))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Token has expired please generate token again");

        System.out.println("⏳ Blocked: token expired.");
    }

    @Test
    @DisplayName("❌ Should return false if user ID doesn't match")
    void shouldReturnFalseIfUserIdDoesNotMatch() {
        User user = User.builder()
                .idUser(2L)
                .tokenExpiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        var auth = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(accessGuard.checkOwner(1L)).isFalse();
        System.out.println("🚫 Access denied: wrong user ID.");
    }
}
