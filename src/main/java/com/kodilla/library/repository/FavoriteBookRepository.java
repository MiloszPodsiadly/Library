package com.kodilla.library.repository;
import com.kodilla.library.model.FavoriteBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
@Repository
public interface FavoriteBookRepository extends CrudRepository<FavoriteBook, Long> {

    boolean existsByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);

    List<FavoriteBook> findAllByBook_IdBook(Long idBook);

    List<FavoriteBook> findAllByUser_IdUser(Long idUser);

    Optional<FavoriteBook> findByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);

    Long countByBook_IdBook(Long idBook);

    void deleteByUser_IdUserAndBook_IdBook(Long idUser, Long idBook);
}

