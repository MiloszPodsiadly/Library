package com.kodilla.library.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "fine_issued")
    private Boolean fineIssued = false;

    @Builder.Default
    @Setter
    private Integer extensionCount = 0;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter
    private Fine fine;

}

