package com.kodilla.library.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.ReservationNotAllowedException;
import com.kodilla.library.exception.ReservationNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.ReservationRepository;
import com.kodilla.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
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

    public void cancelReservation(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ReservationNotFoundException(idReservation));

        if (!isCancelable(reservation)) {
            throw new ReservationNotAllowedException("Booking cannot be canceled for more than 1 hour.");
        }

        deactivateReservation(reservation);
        updateBookStatusIfNoActiveReservations(reservation.getBook());
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
    public void deleteReservation(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ReservationNotFoundException(idReservation));

        LocalDateTime now = LocalDateTime.now();

        boolean isCreatedLessThanHourAgo = reservation.getCreatedAt()
                .plusHours(1)
                .isAfter(now);

        boolean isPastEndDate = reservation.getEndDate().isBefore(now);

        if (reservation.getActive()) {
            if (!isCreatedLessThanHourAgo) {
                throw new ReservationNotAllowedException("Cannot delete active reservation older than 1 hour");
            }
        } else {
            if (!isPastEndDate) {
                throw new ReservationNotAllowedException("Cannot delete inactive reservation before its end date");
            }
        }

        reservationRepository.delete(reservation);
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void checkUnavailableBooksAndNotify() {
        LocalDateTime now = LocalDateTime.now();

        List<Book> notAvailableBooks = bookRepository.findAll().stream()
                .filter(book -> book.getStatuses().contains(BookStatus.NOT_RETURNED))
                .toList();

        for (Book book : notAvailableBooks) {
            List<Reservation> futureReservations = reservationRepository
                    .findAllByBook_IdBook(book.getIdBook())
                    .stream()
                    .filter(res -> res.getStartDate().isAfter(now))
                    .filter(res -> !res.getUnavailableNotificationSent())
                    .toList();

            for (Reservation reservation : futureReservations) {
                System.out.println("[NOTIFICATION] Reservation for book '" + book.getTitle()
                        + "' was canceled for user: " + reservation.getUser().getEmail()
                        + " because another user didn't return the book on time.");

                reservation.setActive(false);
                reservation.setUnavailableNotificationSent(true);
                reservationRepository.save(reservation);
            }
        }
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void expireOldReservations() {
        List<Reservation> activeReservations = reservationRepository.findAllByActiveTrue();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : activeReservations) {
            if (reservation.getEndDate().isBefore(now)) {
                reservation.setActive(false);
                reservationRepository.save(reservation);

                Book book = reservation.getBook();
                boolean anyStillActive = reservationRepository
                        .findAllByBook_IdBook(book.getIdBook())
                        .stream()
                        .anyMatch(Reservation::getActive);

                if (!anyStillActive) {
                    book.setAvailable(true);
                    book.getStatuses().clear();
                    book.getStatuses().add(BookStatus.AVAILABLE);
                    bookRepository.save(book);
                }
            }
        }
    }
}
