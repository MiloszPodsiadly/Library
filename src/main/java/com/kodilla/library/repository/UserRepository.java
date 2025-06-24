package com.kodilla.library.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.kodilla.library.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.*;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAll();
}

