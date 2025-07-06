package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.model.*;
import com.kodilla.library.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;

    public Loan loanBook(Long idUser, Long idBook) throws UserNotFoundByIdException, BookNotFoundByIdException, LoanNotAllowedException {
        User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundByIdException(idUser));
        Book book = bookRepository.findById(idBook).orElseThrow(() -> new BookNotFoundByIdException(idBook));

        Reservation reservation = reservationRepository
                .findAllByBook_IdBook(idBook)
                .stream()
                .filter(r -> r.getUser().getIdUser().equals(idUser))
                .filter(Reservation::getActive)
                .findFirst()
                .orElseThrow(() -> new LoanNotAllowedException("No active reservation found for this user and book."));

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(reservation.getStartDate()) || now.isAfter(reservation.getEndDate())) {
            throw new LoanNotAllowedException("Loan allowed only during reservation window: " + reservation.getStartDate() + " to " + reservation.getEndDate());
        }

        long activeLoans = loanRepository.countByUser_IdUserAndReturnedFalse(idUser);
        if (activeLoans >= 3) {
            throw new LoanNotAllowedException("User has reached max 3 active loans.");
        }
        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(now)
                .returnDate(now.plusHours(6))
                .returned(false)
                .extensionCount(0)
                .fineIssued(false)
                .build();

        book.setAvailable(false);
        book.getStatuses().clear();
        book.getStatuses().add(BookStatus.LOANED);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByUser(Long idUser) throws UserNotFoundByIdException {
        if (!userRepository.existsById(idUser)) {
            throw new UserNotFoundByIdException(idUser);
        }
        return loanRepository.findByUser_IdUser(idUser);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnedFalse();
    }

    public Loan extendLoan(Long idLoan) throws LoanNotFoundByIdException, LoanNotAllowedException {
        Loan loan = loanRepository.findById(idLoan).orElseThrow(() -> new LoanNotFoundByIdException(idLoan));

        if (loan.getReturned()) throw new LoanNotAllowedException("Cannot extend a returned loan.");
        if (LocalDateTime.now().isAfter(loan.getReturnDate())) throw new LoanNotAllowedException("Loan already expired.");
        if (loan.getExtensionCount() >= 1) throw new LoanNotAllowedException("Loan already extended once.");

        Book book = loan.getBook();
        if (book.getStatuses().contains(BookStatus.RESERVED)) {
            throw new LoanNotAllowedException("Cannot extend loan: book is reserved.");
        }

        loan.setReturnDate(loan.getReturnDate().plusHours(6));
        loan.setExtensionCount(loan.getExtensionCount() + 1);
        return loanRepository.save(loan);
    }

    public Loan returnBook(Long idLoan) throws LoanNotFoundByIdException {
        Loan loan = loanRepository.findById(idLoan).orElseThrow(() -> new LoanNotFoundByIdException(idLoan));

        if (loan.getReturned()) return loan;

        loan.setReturned(true);
        loan.setReturnDate(LocalDateTime.now());

        Book book = loan.getBook();
        book.setAvailable(true);
        book.getStatuses().clear();
        book.getStatuses().add(BookStatus.AVAILABLE);
        bookRepository.save(book);

        loanRepository.save(loan);
        checkAndReserveBookIfNeeded(book);

        return loan;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void checkOverdueLoansAndAddFines() {
        LocalDateTime now = LocalDateTime.now();

        List<Loan> overdueLoans = loanRepository.findByReturnedFalse()
                .stream()
                .filter(loan -> loan.getReturnDate().isBefore(now))
                .filter(loan -> !loan.getFineIssued())
                .toList();

        for (Loan loan : overdueLoans) {
            Fine fine = Fine.builder()
                    .loan(loan)
                    .amount(calculateFine(loan))
                    .reason("Book returned late")
                    .issuedDate(now)
                    .user(loan.getUser())
                    .paid(false)
                    .build();

            fineRepository.save(fine);
            loan.setFineIssued(true);
            loanRepository.save(loan);
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void assignReservationToAvailableBooks() {
        LocalDateTime now = LocalDateTime.now();

        List<Book> availableBooks = bookRepository.findAll().stream()
                .filter(book -> Boolean.TRUE.equals(book.getAvailable()))
                .toList();

        for (Book book : availableBooks) {
            Optional<Reservation> nextReservation = reservationRepository
                    .findAllByBook_IdBook(book.getIdBook())
                    .stream()
                    .filter(res -> res != null
                            && res.getActive() != null && res.getActive()
                            && res.getStartDate() != null && !res.getStartDate().isAfter(now)
                            && res.getReservationDate() != null
                            && res.getUser() != null
                            && res.getBook() != null)
                    .min(Comparator.comparing(Reservation::getReservationDate, Comparator.nullsLast(Comparator.naturalOrder())));

            nextReservation.ifPresent(reservation -> {
                book.setAvailable(false);
                book.getStatuses().clear();
                book.getStatuses().add(BookStatus.RESERVED);
                bookRepository.save(book);

                reservation.setActive(true);
                reservationRepository.save(reservation);
            });
        }
    }

    private BigDecimal calculateFine(Loan loan) {
        long daysLate = ChronoUnit.DAYS.between(loan.getReturnDate(), LocalDateTime.now());
        BigDecimal finePerDay = new BigDecimal("2.00");
        return finePerDay.multiply(BigDecimal.valueOf(Math.max(1, daysLate)));
    }

    private void checkAndReserveBookIfNeeded(Book book) {
        LocalDateTime now = LocalDateTime.now();

        Optional<Reservation> nextReservation = reservationRepository
                .findAllByBook_IdBook(book.getIdBook())
                .stream()
                .filter(reservation -> Boolean.TRUE.equals(reservation.getActive()))
                .filter(reservation -> !reservation.getStartDate().isAfter(now))
                .min(Comparator.comparing(Reservation::getReservationDate));

        if (nextReservation.isPresent()) {
            book.setAvailable(false);
            book.getStatuses().clear();
            book.getStatuses().add(BookStatus.RESERVED);
            bookRepository.save(book);

            Reservation reservation = nextReservation.get();
            reservationRepository.save(reservation);
        }
    }
}
