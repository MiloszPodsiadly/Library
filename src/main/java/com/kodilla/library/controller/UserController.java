package com.kodilla.library.controller;

import com.kodilla.library.dto.UserDTO;
import com.kodilla.library.exception.*;
import com.kodilla.library.mapper.UserMapper;
import com.kodilla.library.model.User;
import com.kodilla.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // ✅ GET /users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    // ✅ GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id)
            throws UserNotFoundByIdException {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        User userToSave = userMapper.toEntity(dto);
        User saved = userService.createUser(userToSave);
        return ResponseEntity.ok(userMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO dto
    ) throws UserNotFoundByIdException {

        User updatedUser = userMapper.toEntity(dto);

        // Nadpisujemy ID z URL
        updatedUser.setIdUser(id);

        // Cała logika walidacji i haszowania jest w serwisie
        User saved = userService.updateUser(updatedUser);

        return ResponseEntity.ok(userMapper.toDto(saved));
    }

    // ✅ DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestBody UserDTO authUserDto
    ) throws UserNotFoundByIdException {
        User authUser = userMapper.toEntity(authUserDto); // lub odczytaj z JWT jeśli masz
        userService.deleteUser(id, authUser);
        return ResponseEntity.noContent().build();
    }

    // ✅ POST /users/token (logowanie i token)
    @PostMapping("/token")
    public ResponseEntity<UserDTO> generateToken(
            @RequestParam String email,
            @RequestParam String password
    ) throws UserNotFoundByMailException {
        User user = userService.generateNewToken(email, password);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
