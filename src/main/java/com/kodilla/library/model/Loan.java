package com.kodilla.library.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Table;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime loanDate;
    @Setter
    private LocalDateTime returnDate;

    @Builder.Default
    @Setter
    private Boolean returned = false;

    @Builder.Default
    @Setter
    private Integer extensionCount = 0;

    @OneToOne(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Fine fine;

}

