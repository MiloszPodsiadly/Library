package com.kodilla.library.repository;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserAndDueDateAfter(User user, LocalDate dueDate);
    List<Loan> findByBookAndDueDateBefore(Book book, LocalDate dueDate);
}
