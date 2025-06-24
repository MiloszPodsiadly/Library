package com.kodilla.library.repository;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findByUser_IdUser(Long idUser); // ✅ poprawnie przez relację

    List<Loan> findByReturnedFalse(); // ✅ OK

    long countByUser_IdUserAndReturnedFalse(Long idUser); // ✅ poprawnie przez relację

    long countByBook_IdBook(Long idBook); // ✅ poprawnie przez relację
}

