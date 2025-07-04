package com.kodilla.library.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.exception.UserNotFoundByMailException;
import com.kodilla.library.jwt.JwtService;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.UserRepository;
import com.kodilla.library.security.AccessGuard;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AccessGuard accessGuard;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long idUser) throws UserNotFoundByIdException {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));
    }

    public User getUserByEmail(String email) throws UserNotFoundByMailException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundByMailException(email));
    }

    @Transactional
    public User createUser(User user) {
        user.setIdUser(null);
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .passwordHash(hashedPassword)
                .active(true)
                .build();

        newUser = userRepository.save(newUser);

        String token = jwtService.generateToken(newUser);
        newUser.setToken(token);
        newUser.setTokenCreatedAt(LocalDateTime.now());
        newUser.setTokenExpiresAt(LocalDateTime.now().plusHours(1));

        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(User updated) throws UserNotFoundByIdException {
        accessGuard.checkOwner(updated.getIdUser());

        User existing = getUserById(updated.getIdUser());

        String newPasswordHash = existing.getPasswordHash();
        if (updated.getPasswordHash() != null && !updated.getPasswordHash().isBlank()) {
            newPasswordHash = passwordEncoder.encode(updated.getPasswordHash());
        }

        User modifiedUser = User.builder()
                .idUser(existing.getIdUser())
                .name(updated.getName() != null ? updated.getName() : existing.getName())
                .email(updated.getEmail() != null ? updated.getEmail() : existing.getEmail())
                .passwordHash(newPasswordHash)
                .active(updated.getActive() != null ? updated.getActive() : existing.getActive())
                .token(existing.getToken())
                .tokenCreatedAt(existing.getTokenCreatedAt())
                .tokenExpiresAt(existing.getTokenExpiresAt())
                .loans(existing.getLoans())
                .reservations(existing.getReservations())
                .reviews(existing.getReviews())
                .build();

        return userRepository.save(modifiedUser);
    }

    @Transactional
    public void deleteUser(Long idUser) throws UserNotFoundByIdException {
        if (!accessGuard.checkOwner(idUser)) {
            throw new SecurityException("Unauthorized access.");
        }
        if (!userRepository.existsById(idUser)) {
            throw new UserNotFoundByIdException(idUser);
        }
        userRepository.deleteById(idUser);
    }

    @Transactional
    public User generateNewToken(String email, String rawPassword) throws UserNotFoundByMailException {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        user.setToken(token);
        user.setTokenCreatedAt(LocalDateTime.now());
        user.setTokenExpiresAt(LocalDateTime.now().plusHours(1));

        return userRepository.save(user);
    }
}
