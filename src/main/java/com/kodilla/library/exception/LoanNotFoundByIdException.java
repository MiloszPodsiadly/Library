package com.kodilla.library.exception;

public class LoanNotFoundByIdException extends Exception {
    public LoanNotFoundByIdException(Long idLoan) {
        super("" + idLoan);
    }
}
