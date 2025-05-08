package com.kodilla.library.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    private LocalDate startDate;
    private LocalDate dueDate;

    private boolean extended = false;
    public Loan(Book book, User user, LocalDate startDate, LocalDate dueDate, boolean extended) {
        this.book = book;
        this.user = user;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.extended = extended;
    }
}

