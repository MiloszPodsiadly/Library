package com.kodilla.library.service;

import com.kodilla.library.exception.*;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.BookStatus;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Loan loanBook(Long idUser, Long idBook)
            throws UserNotFoundByIdException, BookNotFoundByIdException, LoanNotAllowedException {

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));

        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

        if (!book.getAvailable()) {
            throw new LoanNotAllowedException("Book is not available.");
        }

        if (book.getStatuses().contains(BookStatus.RESERVED)) {
            throw new LoanNotAllowedException("Book is currently reserved.");
        }

        long activeLoans = loanRepository.countByUser_IdUserAndReturnedFalse(idUser);
        if (activeLoans >= 2) {
            throw new LoanNotAllowedException("User has reached maximum of 2 active loans.");
        }

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now().plusWeeks(2))
                .returned(false)
                .extensionCount(0)
                .build();

        book.setAvailable(false);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public Loan returnBook(Long idLoan) throws LoanNotFoundByIdException {
        Loan loan = loanRepository.findById(idLoan)
                .orElseThrow(() -> new LoanNotFoundByIdException(idLoan));

        if (loan.getReturned()) {
            return loan; // już zwrócona
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDateTime.now());

        Book book = loan.getBook();
        book.setAvailable(true);
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

        Book book = loan.getBook();
        if (book.getStatuses().contains(BookStatus.RESERVED)) {
            throw new LoanNotAllowedException("Cannot extend loan: book is reserved.");
        }

        if (loan.getExtensionCount() >= 1) {
            throw new LoanNotAllowedException("Loan has already been extended.");
        }

        loan.setReturnDate(loan.getReturnDate().plusWeeks(1));
        loan.setExtensionCount(loan.getExtensionCount() + 1);

        return loanRepository.save(loan);
    }
}

