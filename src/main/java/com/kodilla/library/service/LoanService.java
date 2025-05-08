package com.kodilla.library.service;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.LoanRepository;
import com.kodilla.library.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import com.kodilla.library.dto.LoanDto;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.Loan;
import com.kodilla.library.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, ReservationRepository reservationRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
    }

    public LoanDto createLoan(User user, Long bookId, int loanDays) {
        // Tworzenie obiektu Loan
        Book book = bookRepository.findByIdAndAvailableTrue(bookId)
                .orElseThrow(() -> new RuntimeException("Book not available"));

        if (reservationRepository.findByBookAndUser(book, user).isPresent()) {
            throw new RuntimeException("You have already reserved this book");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate dueDate = startDate.plusDays(loanDays);

        Loan loan = new Loan(book, user, startDate, dueDate, false);

        // Aktualizacja stanu książki i zapis
        book.setAvailable(false);
        bookRepository.save(book);

        // Zapis wypożyczenia w repozytorium
        loanRepository.save(loan);

        // Zwracamy LoanDto
        return new LoanDto(loan.getId(), loan.getBook().getId(), loan.getUser().getId(),
                loan.getStartDate(), loan.getDueDate(), loan.isExtended());
    }

    public LoanDto extendLoan(Long loanId, int extraDays) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        Book book = loan.getBook();

        // Sprawdzanie, czy książka jest zarezerwowana przez innego użytkownika
        if (reservationRepository.findByBookAndReservationDateAfter(book, LocalDate.now()).size() > 0) {
            throw new RuntimeException("Book is reserved by another user, cannot extend");
        }

        loan.setDueDate(loan.getDueDate().plusDays(extraDays)); // Przedłużenie terminu wypożyczenia
        loan.setExtended(true);

        loanRepository.save(loan);

        return toDto(loan); // Zwracamy LoanDto
    }

    // Helper method to convert Loan to LoanDto
    private LoanDto toDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getUser().getId(),
                loan.getStartDate(),
                loan.getDueDate(),
                loan.isExtended()
        );
    }
}

