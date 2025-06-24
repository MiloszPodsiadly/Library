package com.kodilla.library.repository;
import com.kodilla.library.model.Fine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
@Repository
public interface FineRepository extends CrudRepository<Fine, Long> {
    Optional<Fine> findByLoan_IdLoan(Long loanId);
    @Query("SELECT f FROM Fine f WHERE f.loan.returned = false")
    List<Fine> findAllWithUnreturnedLoans();
    List<Fine> findAllByUser_IdUser(Long idUser);
}
