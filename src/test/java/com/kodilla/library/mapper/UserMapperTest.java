package com.kodilla.library.mapper;

import com.kodilla.library.dto.UserDTO;
import com.kodilla.library.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;
    private User user;

    @BeforeEach
    void setup() {
        mapper = new UserMapper();

        user = User.builder()
                .idUser(1L)
                .name("Alice")
                .email("alice@example.com")
                .active(true)
                .token("mock-token")
                .tokenCreatedAt(LocalDateTime.of(2024, 6, 1, 10, 0))
                .tokenExpiresAt(LocalDateTime.of(2024, 6, 1, 11, 0))
                .passwordHash("encodedPassword")
                .build();

        System.out.println("🔧 Initialized test data for UserMapper.");
    }

    @Test
    @DisplayName("👤 Should map User to UserDTO")
    void shouldMapToDto() {
        UserDTO dto = mapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo("Alice");
        assertThat(dto.email()).isEqualTo("alice@example.com");
        assertThat(dto.active()).isTrue();
        assertThat(dto.token()).isEqualTo("mock-token");

        System.out.println("✅ Successfully mapped User to UserDTO.");
    }

    @Test
    @DisplayName("📥 Should map UserDTO to User")
    void shouldMapToEntity() {
        UserDTO dto = new UserDTO(
                "Bob",
                "bob@example.com",
                true,
                "jwt-token",
                LocalDateTime.of(2024, 6, 2, 10, 0),
                LocalDateTime.of(2024, 6, 2, 11, 0),
                "plainPassword"
        );

        User result = mapper.toEntity(dto);

        assertThat(result.getName()).isEqualTo("Bob");
        assertThat(result.getEmail()).isEqualTo("bob@example.com");
        assertThat(result.getToken()).isEqualTo("jwt-token");
        assertThat(result.getPasswordHash()).isEqualTo("plainPassword"); // raw password, not encoded here

        System.out.println("✅ Successfully mapped UserDTO to User.");
    }

    @Test
    @DisplayName("📋 Should map list of Users to list of DTOs")
    void shouldMapUserListToDtoList() {
        List<UserDTO> result = mapper.toDtoList(List.of(user));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("alice@example.com");

        System.out.println("📋 Successfully mapped User list to DTO list.");
    }
}
