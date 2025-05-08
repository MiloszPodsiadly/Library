package com.kodilla.library.service;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import com.kodilla.library.dto.ReservationDto;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.kodilla.library.model.UserEntity;
import com.kodilla.library.repository.UserRepository;
import com.kodilla.library.service.UserService;
import com.kodilla.library.controller.ReservationController;




@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository,UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository =userRepository;
    }

    // Tworzenie rezerwacji i zwracanie DTO
    public ReservationDto createReservation(User user, Long bookId) {
        Book book = bookRepository.findByIdAndAvailableTrue(bookId)
                .orElseThrow(() -> new RuntimeException("Book not available"));

        if (reservationRepository.findByBookAndUser(book, user).isPresent()) {
            throw new RuntimeException("You have already reserved this book");
        }

        Reservation reservation = new Reservation(book, user, LocalDate.now());
        reservationRepository.save(reservation);

        return toDto(reservation);  // Zwracamy DTO
    }

    // Pobranie rezerwacji dla książki
    public List<ReservationDto> getReservationsForBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        List<Reservation> reservations = reservationRepository.findByBookAndReservationDateAfter(book, LocalDate.now());
        return reservations.stream().map(this::toDto).collect(Collectors.toList());
    }

    //Pobranie rezerwacji dla użytkownika
    public List<ReservationDto> getUserReservations(Long id) {
        // Znajdź użytkownika na podstawie id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Pobierz wszystkie rezerwacje dla użytkownika
        List<Reservation> reservations = reservationRepository.findByUser(user);

        // Zamień listę rezerwacji na listę DTO
        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Reservation to ReservationDto
    private ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getBook().getId(),
                reservation.getUser().getId(),
                reservation.getReservationDate()
        );
    }

}
