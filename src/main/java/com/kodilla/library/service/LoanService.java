package com.kodilla.library.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.LoanNotAllowedException;
import com.kodilla.library.exception.LoanNotFoundByIdException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.Reservation;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.ReservationRepository;
import com.kodilla.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository; // Dodane

    public Loan loanBook(Long idUser, Long idBook)
            throws UserNotFoundByIdException, BookNotFoundByIdException, LoanNotAllowedException {

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

        Reservation reservation = reservationRepository
                .findAllByBook_IdBook(idBook)
                .stream()
                .filter(r -> r.getUser().getIdUser().equals(idUser))
                .filter(Reservation::getActive)
                .findFirst()
                .orElseThrow(() -> new LoanNotAllowedException("No active reservation found for this user and book."));

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(reservation.getStartDate()) || now.isAfter(reservation.getEndDate())) {
            throw new LoanNotAllowedException("You can only loan the book during your reservation window: " +
                    reservation.getStartDate() + " to " + reservation.getEndDate());
        }

        long activeLoans = loanRepository.countByUser_IdUserAndReturnedFalse(idUser);
        if (activeLoans >= 3) {
            throw new LoanNotAllowedException("User has reached maximum of 3 active loans.");
        }

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(now)
                .returnDate(now.plusHours(6))
                .returned(false)
                .extensionCount(0)
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
        Loan loan = loanRepository.findById(idLoan)
                .orElseThrow(() -> new LoanNotFoundByIdException(idLoan));

        if (loan.getReturned()) {
            throw new LoanNotAllowedException("Cannot extend a returned loan.");
        }

        if (LocalDateTime.now().isAfter(loan.getReturnDate())) {
            throw new LoanNotAllowedException("Cannot extend a loan that has already expired.");
        }

        Book book = loan.getBook();
        if (book.getStatuses().contains(BookStatus.RESERVED)) {
            throw new LoanNotAllowedException("Cannot extend loan: book is reserved.");
        }

        if (loan.getExtensionCount() >= 1) {
            throw new LoanNotAllowedException("Loan has already been extended.");
        }

        loan.setReturnDate(loan.getReturnDate().plusHours(6));
        loan.setExtensionCount(loan.getExtensionCount() + 1);

        return loanRepository.save(loan);
    }


    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void expireOldLoans() {
        LocalDateTime now = LocalDateTime.now();

        List<Loan> expiredLoans = loanRepository.findByReturnedFalse()
                .stream()
                .filter(loan -> loan.getReturnDate().isBefore(now))
                .toList();

        for (Loan loan : expiredLoans) {
            loan.setReturned(true);
            loanRepository.save(loan);

            Book book = loan.getBook();
            book.setAvailable(true);
            book.getStatuses().clear();
            book.getStatuses().add(BookStatus.AVAILABLE);
            bookRepository.save(book);
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void markReservedBooksAfterLoanEnd() {

        List<Book> availableBooks = bookRepository.findAll().stream()
                .filter(Book::getAvailable)
                .toList();

        for (Book book : availableBooks) {
            boolean hasActiveReservation = reservationRepository
                    .findAllByBook_IdBook(book.getIdBook())
                    .stream()
                    .anyMatch(Reservation::getActive);

            if (hasActiveReservation) {
                book.setAvailable(false);
                book.getStatuses().clear();
                book.getStatuses().add(BookStatus.RESERVED);
                bookRepository.save(book);
            }
        }
    }

}
