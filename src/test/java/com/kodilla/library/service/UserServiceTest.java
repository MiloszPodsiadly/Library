package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.jwt.JwtService;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.UserRepository;
import com.kodilla.library.security.AccessGuard;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    @Mock private AccessGuard accessGuard;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .idUser(1L)
                .name("Alice")
                .email("alice@example.com")
                .passwordHash("hashedPass")
                .active(true)
                .build();
        System.out.println("🔧 Initialized mocks for UserService.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("✅ UserService test finished.\n");
    }

    @Test
    @DisplayName("👤 Should fetch user by ID")
    void shouldGetUserById() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        System.out.println("📥 User fetched by ID: " + result.getName());
    }

    @Test
    @DisplayName("❌ Should throw if user not found by ID")
    void shouldThrowWhenUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundByIdException.class);
        System.out.println("❌ User not found by ID.");
    }

    @Test
    @DisplayName("📧 Should fetch user by email")
    void shouldGetUserByEmail() throws Exception {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("alice@example.com");

        assertThat(result.getName()).isEqualTo("Alice");
        System.out.println("📧 User fetched by email.");
    }

    @Test
    @DisplayName("❌ Should throw if user not found by email")
    void shouldThrowWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("nope@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("nope@example.com"))
                .isInstanceOf(UserNotFoundByMailException.class);
        System.out.println("❌ User not found by email.");
    }

    @Test
    @DisplayName("🆕 Should create user with encoded password and token")
    void shouldCreateUser() {
        User input = User.builder().name("Bob").email("bob@example.com").passwordHash("plainPass").build();
        when(passwordEncoder.encode("plainPass")).thenReturn("hashedBob");
        when(jwtService.generateToken(any())).thenReturn("token123");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(input);

        assertThat(result.getPasswordHash()).isEqualTo("hashedBob");
        assertThat(result.getToken()).isEqualTo("token123");
        assertThat(result.getTokenCreatedAt()).isNotNull();
        System.out.println("🆕 User created with token: " + result.getToken());
    }

    @Test
    @DisplayName("🔄 Should update user name and hash new password")
    void shouldUpdateUser() throws Exception {
        User updated = User.builder()
                .idUser(1L)
                .name("NewName")
                .passwordHash("newPass")
                .build();

        when(accessGuard.checkOwner(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("newHashed");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(updated);

        assertThat(result.getName()).isEqualTo("NewName");
        assertThat(result.getPasswordHash()).isEqualTo("newHashed");
        System.out.println("🔄 User updated: " + result.getName());
    }

    @Test
    @DisplayName("🗝️ Should generate new token for valid credentials")
    void shouldGenerateNewToken() throws Exception {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newToken123");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.generateNewToken("alice@example.com", "pass123");

        assertThat(result.getToken()).isEqualTo("newToken123");
        assertThat(result.getTokenExpiresAt()).isAfter(LocalDateTime.now());
        System.out.println("🗝️ New token generated: " + result.getToken());
    }

    @Test
    @DisplayName("❌ Should throw when credentials are invalid")
    void shouldThrowInvalidCredentials() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashedPass")).thenReturn(false);

        assertThatThrownBy(() -> userService.generateNewToken("alice@example.com", "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid credentials");

        System.out.println("❌ Invalid credentials provided.");
    }

    @Test
    @DisplayName("🗑️ Should delete user if access granted")
    void shouldDeleteUser() throws Exception {
        when(accessGuard.checkOwner(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        System.out.println("🗑️ User deleted.");
    }

    @Test
    @DisplayName("⛔ Should throw if unauthorized delete attempt")
    void shouldBlockUnauthorizedDelete() {
        when(accessGuard.checkOwner(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(SecurityException.class);

        System.out.println("⛔ Unauthorized delete attempt blocked.");
    }
}
