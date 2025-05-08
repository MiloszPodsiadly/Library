package com.kodilla.library;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.kodilla.library.service.AuthService;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorizationServiceTestSuite {

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicjalizacja Mocków
    }


    @Test
    void shouldLoadUserByUsername() {
        // Przygotowanie
        String username = "user";
        UserDetails userDetails = User.withUsername(username)
                .password("password")
                .roles("USER")
                .build();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Akcja
        UserDetails result = authService.loadUserByUsername(username);

        // Weryfikacja
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userDetailsService, times(1)).loadUserByUsername(username);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Przygotowanie
        String username = "not_found_user";
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        // Akcja i weryfikacja
        assertThrows(IllegalArgumentException.class, () -> authService.loadUserByUsername(username));
    }
}
