package com.kodilla.library.repository;

import com.kodilla.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash("secret")
                .active(true)
                .build();

        userRepository.save(user);
        System.out.println("✅ Setup complete: test user saved.");
    }

    @Test
    @DisplayName("🔍 Should find user by email")
    void shouldFindByEmail() {
        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test User");
        System.out.println("📧 User found by email: " + result.get().getEmail());
    }

    @Test
    @DisplayName("🆔 Should find user by idUser")
    void shouldFindByIdUser() {
        Optional<User> result = userRepository.findByIdUser(user.getIdUser());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        System.out.println("🆔 User found by idUser: " + result.get().getIdUser());
    }

    @Test
    @DisplayName("📋 Should return all users")
    void shouldReturnAllUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(1);
        System.out.println("📋 Total users found: " + users.size());
    }
}
