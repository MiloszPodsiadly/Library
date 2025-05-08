package com.kodilla.library.repository;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBookAndReservationDateAfter(Book book, LocalDate reservationDate);
    Optional<Reservation> findByBookAndUser(Book book, User user);
    List<Reservation> findByUser(User user);

}
