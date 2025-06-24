package com.kodilla.library.mapper;

import com.kodilla.library.dto.UserDTO;
import com.kodilla.library.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getActive(),
                user.getToken(),
                user.getTokenCreatedAt(),
                user.getTokenExpiresAt(),
                null // ❌ nie zwracamy rawPassword w odpowiedzi!
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .active(dto.active())
                .token(dto.token())
                .tokenCreatedAt(dto.tokenCreatedAt())
                .tokenExpiresAt(dto.tokenExpiresAt())
                .passwordHash(dto.rawPassword()) // ✅ rawPassword trafi do pola passwordHash
                .build();
    }

    public List<UserDTO> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
