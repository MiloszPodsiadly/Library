package com.kodilla.library.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.BookStatistics;
@Repository
public interface BookStatisticsRepository extends CrudRepository<BookStatistics, Long> {
    Optional<BookStatistics> findByBook_IdBook(Long idBook);
}

