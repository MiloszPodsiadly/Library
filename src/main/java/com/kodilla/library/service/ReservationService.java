package com.kodilla.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.ReservationNotAllowedException;
import com.kodilla.library.exception.ReservationNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.ReservationRepository;
import com.kodilla.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Reservation reserveBook(Long idUser, Long idBook)
            throws BookNotFoundByIdException, UserNotFoundByIdException {

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

        boolean alreadyReserved = reservationRepository
                .existsByUser_IdUserAndBook_IdBookAndActiveTrue(idUser, idBook);
        if (alreadyReserved) {
            throw new IllegalStateException("User already has an active reservation for this book.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationStart = calculateNextAvailableDate(book.getIdBook());
        LocalDateTime endDate = reservationStart.plusHours(6);

        long reservationOrder = reservationRepository.countByBook_IdBook(idBook);

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .reservationDate(reservationStart)
                .startDate(reservationStart)
                .endDate(endDate)
                .active(true)
                .reservationOrder((int) reservationOrder)
                .createdAt(now)
                .build();

        book.setAvailable(false);
        book.getStatuses().clear();
        book.getStatuses().add(BookStatus.RESERVED);
        bookRepository.save(book);

        return reservationRepository.save(reservation);
    }

    public LocalDateTime calculateNextAvailableDate(Long idBook) {
        List<Reservation> reservations = reservationRepository
                .findAllByBook_IdBook(idBook)
                .stream()
                .filter(Reservation::getActive) // tylko aktywne rezerwacje
                .toList();

        if (reservations.isEmpty()) {
            return LocalDateTime.now();
        }

        LocalDateTime lastReservationEndDate = reservations.stream()
                .map(Reservation::getEndDate)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        return lastReservationEndDate.plusDays(1)
                .withHour(10).withMinute(0).withSecond(0);
    }

    private boolean isCancelable(Reservation reservation) {
        return reservation.getCreatedAt()
                .plusHours(1)
                .isAfter(LocalDateTime.now());
    }

    private void deactivateReservation(Reservation reservation) {
        reservation.setActive(false);
        reservationRepository.save(reservation);
    }

    private void updateBookStatusIfNoActiveReservations(Book book) {
        boolean hasActiveReservations = reservationRepository
                .findAllByBook_IdBook(book.getIdBook())
                .stream()
                .anyMatch(Reservation::getActive);

        if (!hasActiveReservations) {
            book.setAvailable(true);
            book.getStatuses().clear();
            book.getStatuses().add(BookStatus.AVAILABLE);
            bookRepository.save(book);
        }
    }

    @Transactional
    public void cancelReservation(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + idReservation));

        LocalDateTime now = LocalDateTime.now();

        if (reservation.getStartDate() == null) {
            throw new IllegalStateException("Reservation start date is not set.");
        }

        if (now.isAfter(reservation.getStartDate().minusHours(1))) {
            throw new IllegalStateException("You can only cancel the reservation up to 1 hour before it starts.");
        }

        reservation.setActive(false);
        reservation.setEndDate(now);
        reservationRepository.save(reservation);
    }


    public List<Reservation> getReservationsByUser(Long userId) throws UserNotFoundByIdException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }
        return reservationRepository.findAllByUser_IdUser(userId);
    }

    public List<Reservation> getReservationsForBook(Long idBook) throws BookNotFoundByIdException {
        if (!bookRepository.existsById(idBook)) {
            throw new BookNotFoundByIdException(idBook);
        }
        return reservationRepository.findAllByBook_IdBook(idBook);
    }

    @Transactional
    public void deleteReservation(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + idReservation));

        LocalDateTime now = LocalDateTime.now();

        if (Boolean.TRUE.equals(reservation.getActive())) {
            throw new IllegalStateException("Cannot delete an active reservation.");
        }

        if (reservation.getEndDate() == null || reservation.getEndDate().isAfter(now)) {
            throw new IllegalStateException("Cannot delete reservation that has not ended yet.");
        }

        reservationRepository.deleteById(idReservation);
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    public void manageReservationsAndAvailability() {
        LocalDateTime now = LocalDateTime.now();

        StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .filter(res -> !Boolean.TRUE.equals(res.getActive()))
                .filter(res -> res.getStartDate() != null && !res.getStartDate().isAfter(now))
                .forEach(res -> {
                    res.setActive(true);
                    reservationRepository.save(res);
                });

        reservationRepository.findAllByActiveTrue().stream()
                .filter(res -> res.getEndDate() != null && res.getEndDate().isBefore(now))
                .forEach(res -> {
                    res.setActive(false);
                    reservationRepository.save(res);
                });

        List<Book> notReturnedBooks = bookRepository.findAll().stream()
                .filter(book -> book.getStatuses() != null && book.getStatuses().contains(BookStatus.NOT_RETURNED))
                .toList();

        for (Book book : notReturnedBooks) {
            reservationRepository.findAllByBook_IdBook(book.getIdBook()).stream()
                    .filter(res -> res.getStartDate() != null && res.getStartDate().isBefore(now.plusDays(1)))
                    .filter(res -> !Boolean.TRUE.equals(res.getUnavailableNotificationSent()))
                    .forEach(res -> {
                        System.out.println("[NOTIFICATION] Reservation for book '" + book.getTitle()
                                + "' was canceled for user: " + res.getUser().getEmail());

                        res.setActive(false);
                        res.setUnavailableNotificationSent(true);
                        reservationRepository.save(res);
                    });
        }

        List<Book> allBooks = bookRepository.findAll();

        for (Book book : allBooks) {
            boolean hasActiveReservation = reservationRepository
                    .findAllByBook_IdBook(book.getIdBook()).stream()
                    .anyMatch(res -> Boolean.TRUE.equals(res.getActive()));

            boolean hasActiveLoan = loanRepository
                    .existsByBook_IdBookAndReturnedFalse(book.getIdBook());

            if (!hasActiveReservation && !hasActiveLoan) {
                book.setAvailable(true);
                book.getStatuses().clear();
                book.getStatuses().add(BookStatus.AVAILABLE);
                bookRepository.save(book);
            }
        }
    }
}

