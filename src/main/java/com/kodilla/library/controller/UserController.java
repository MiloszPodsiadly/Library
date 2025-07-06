package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodilla.library.dto.UserDTO;
import com.kodilla.library.exception.UserNotFoundByMailException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.UserMapper;
import com.kodilla.library.model.User;
import com.kodilla.library.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        User user = userService.getUserById(idUser);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toDto(createdUser));
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long idUser,
            @RequestBody UserDTO dto
    ) throws UserNotFoundByIdException {
        User updatedUser = userMapper.toEntity(dto);
        updatedUser.setIdUser(idUser);
        User saved = userService.updateUser(updatedUser);
        return ResponseEntity.ok(userMapper.toDto(saved));
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<String> deleteUser(@PathVariable Long idUser) throws UserNotFoundByIdException {
        userService.deleteUser(idUser);
        return ResponseEntity.ok("User has been deleted");
    }

    @PostMapping("/token")
    public ResponseEntity<UserDTO> generateToken(@RequestBody UserDTO userDTO)
            throws UserNotFoundByMailException {

        String email = userDTO.email();
        String passwordHash = userDTO.passwordHash();

        User user = userService.generateNewToken(email, passwordHash);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
