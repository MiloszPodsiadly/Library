package com.kodilla.library.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.Fine;
@Repository
public interface FineRepository extends CrudRepository<Fine, Long> {
    List<Fine> findAllByUser_IdUser(Long idUser);
}
