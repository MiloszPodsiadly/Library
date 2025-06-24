package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.model.*;
import com.kodilla.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Reservation reserveBook(Long idUser, Long idBook)
            throws BookNotFoundByIdException, UserNotFoundByIdException {

        // 1. Znajdź użytkownika i książkę
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

        // 2. Sprawdź, czy użytkownik już ma aktywną rezerwację tej książki
        boolean alreadyReserved = reservationRepository.existsByUser_IdUserAndBook_IdBookAndActiveTrue(idUser, idBook);
        if (alreadyReserved) {
            throw new IllegalStateException("User already has an active reservation for this book.");
        }

        // 3. Wylicz datę rozpoczęcia nowej rezerwacji
        LocalDateTime reservationStart = calculateNextAvailableDate(book.getIdBook());

        // 4. Oblicz kolejność rezerwacji (np. 0, 1, 2...)
        long reservationOrder = reservationRepository.countByBook_IdBook(idBook);

        // 5. Zbuduj rezerwację
        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .reservationDate(reservationStart)
                .active(true)
                .reservationOrder((int) reservationOrder)
                .createdAt(LocalDateTime.now())
                .build();

        // 6. Zaktualizuj status książki
        book.getStatuses().add(BookStatus.RESERVED);
        bookRepository.save(book);

        // 7. Zapisz rezerwację
        return reservationRepository.save(reservation);
    }

    public LocalDateTime calculateNextAvailableDate(Long idBook) {
        // 1. Pobierz wszystkie rezerwacje dla danej książki (zarówno aktywne, jak i zakończone)
        List<Reservation> reservations = reservationRepository.findAllByBook_IdBook(idBook);

        // 2. Jeśli książka nie ma jeszcze żadnych rezerwacji – uznaj, że dostępna jest od razu
        if (reservations.isEmpty()) {
            return LocalDateTime.now()
                    .withHour(10).withMinute(0).withSecond(0).withNano(0); // normalizujemy godzinę
        }

        // 3. Znajdź najpóźniejszą datę zakończenia rezerwacji
        LocalDateTime lastReservationEndDate = reservations.stream()
                .map(Reservation::getEndDate) // pobieramy daty zakończenia
                .max(LocalDateTime::compareTo) // wybieramy najpóźniejszą
                .orElse(LocalDateTime.now());  // fallback jeśli coś pójdzie nie tak (powinno być niemożliwe)

        // 4. Ustaw dostępność książki na dzień po ostatniej rezerwacji
        return lastReservationEndDate.plusDays(1)
                .withHour(10).withMinute(0).withSecond(0).withNano(0); // godzina otwarcia wypożyczalni
    }


    public void cancelReservation(Long idReservation) throws ReservationNotFoundException {
        // Znajdź rezerwację lub rzuć wyjątek
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ReservationNotFoundException(idReservation));

        // Sprawdź, czy anulowanie jest dozwolone (maksymalnie 1 godzina od utworzenia)
        if (reservation.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now())) {
            throw new ReservationNotAllowedException("You can only cancel within 1 hour of reservation.");
        }

        // Ustaw rezerwację jako nieaktywną
        reservation.setActive(false);
        reservationRepository.save(reservation);

        // Sprawdź, czy książka ma jakiekolwiek aktywne rezerwacje — jeśli nie, usuń status RESERVED
        Book book = reservation.getBook();
        boolean anyStillActive = reservationRepository
                .findAllByBook_IdBook(book.getIdBook())
                .stream()
                .anyMatch(Reservation::getActive);

        if (!anyStillActive) {
            book.getStatuses().remove(BookStatus.RESERVED);
            bookRepository.save(book);
        }
    }

    public List<Reservation> getReservationsByUser(Long userId) throws UserNotFoundByIdException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }
        return reservationRepository.findAllByUser_IdUser(userId);
    }

    public List<Reservation> getReservationsForBook(Long bookId) throws BookNotFoundByIdException {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundByIdException(bookId);
        }
        return reservationRepository.findAllByBook_IdBook(bookId);
    }
}
