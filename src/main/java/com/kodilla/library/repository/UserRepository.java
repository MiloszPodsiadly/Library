package com.kodilla.library.repository;

import com.kodilla.library.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kodilla.library.model.User;
import java.util.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<UserEntity> findByUsername(String username);
    Optional<User> findById(Long id);

}
