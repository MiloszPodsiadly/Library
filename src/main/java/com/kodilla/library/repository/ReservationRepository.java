package com.kodilla.library.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAllByUser_IdUser(Long idUser);

    Long countByBook_IdBook(Long idBook);

    List<Reservation> findAllByBook_IdBook(Long idBook);

    boolean existsByUser_IdUserAndBook_IdBookAndActiveTrue(Long idUser, Long idBook);

    List<Reservation> findAllByActiveTrue();
}

