package com.kodilla.library.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kodilla.library.model.Loan;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findByUser_IdUser(Long idUser);

    List<Loan> findByReturnedFalse();

    long countByUser_IdUserAndReturnedFalse(Long idUser);

    long countByBook_IdBook(Long idBook);
}

