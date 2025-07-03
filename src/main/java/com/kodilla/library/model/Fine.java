package com.kodilla.library.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFine;

    @OneToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private BigDecimal amount;

    private String reason;

    private LocalDateTime issuedDate;

    @Builder.Default
    private Boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
