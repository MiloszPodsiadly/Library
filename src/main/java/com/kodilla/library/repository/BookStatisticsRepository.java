package com.kodilla.library.repository;
import com.kodilla.library.model.BookStatistics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface BookStatisticsRepository extends CrudRepository<BookStatistics, Long> {
    Optional<BookStatistics> findByBook_IdBook(Long idBook);
}

