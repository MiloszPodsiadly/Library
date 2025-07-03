package com.kodilla.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.FavoriteBook;
@Repository
public interface FavoriteBookRepository extends CrudRepository<FavoriteBook, Long> {

    boolean existsByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);

    List<FavoriteBook> findAllByBook_IdBook(Long idBook);

    List<FavoriteBook> findAllByUser_IdUser(Long idUser);

    Optional<FavoriteBook> findByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);

    Long countByBook_IdBook(Long idBook);

    void deleteByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);
}

