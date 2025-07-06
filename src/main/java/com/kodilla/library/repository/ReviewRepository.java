package com.kodilla.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.Review;
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByBook_IdBook(Long idBook);
    List<Review> findByUser_IdUser(Long idUser);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.idBook = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);
    boolean existsByUser_IdUserAndBook_IdBook(Long userId, Long bookId);
}

