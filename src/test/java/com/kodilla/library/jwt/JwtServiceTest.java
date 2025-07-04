package com.kodilla.library.jwt;

import com.kodilla.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Ustawiamy sztuczny secretKey do testów
        String testSecret = "testSecretKey123456789012345678901234567890";
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecret);

        user = User.builder()
                .idUser(1L)
                .email("test@example.com")
                .name("Tester")
                .build();
    }

    @Test
    @DisplayName("🔐 Should generate valid JWT for user")
    void shouldGenerateValidToken() {
        String token = jwtService.generateToken(user);

        assertThat(token).isNotBlank();
        assertThat(jwtService.isTokenValid(token)).isTrue();
        System.out.println("✅ Token generated and validated: " + token);
    }

    @Test
    @DisplayName("🆔 Should extract user ID from token")
    void shouldExtractUserId() {
        String token = jwtService.generateToken(user);
        Long userId = jwtService.extractUserId(token);

        assertThat(userId).isEqualTo(1L);
        System.out.println("🆔 Extracted user ID: " + userId);
    }

    @Test
    @DisplayName("⏳ Should get expiration date from token")
    void shouldGetExpirationDate() {
        String token = jwtService.generateToken(user);
        Optional<Date> expiration = jwtService.getExpirationDate(token);

        assertThat(expiration).isPresent();
        assertThat(expiration.get()).isAfter(new Date());
        System.out.println("⏳ Expiration: " + expiration.get());
    }

    @Test
    @DisplayName("🚫 Should detect invalid token")
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "thisIsNotARealToken";
        boolean result = jwtService.isTokenValid(invalidToken);

        assertThat(result).isFalse();
        System.out.println("🚫 Invalid token correctly rejected.");
    }
}
