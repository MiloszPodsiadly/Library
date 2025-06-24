package com.kodilla.library.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
@Entity
@Getter
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

    @Setter
    private BigDecimal amount;

    @Setter
    private String reason;

    private LocalDateTime issuedDate;

    @Builder.Default
    @Setter
    private Boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

