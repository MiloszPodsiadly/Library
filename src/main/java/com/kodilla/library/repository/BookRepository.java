package com.kodilla.library.repository;

import com.kodilla.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
}

