package com.kodilla.library.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder.Default
    @Setter
    private Boolean active = true;

    @Builder.Default
    @JoinColumn(name = "reservation_count")
    private Integer reservationOrder = 1;

    @Column(name = "start_date")
    private LocalDateTime startDate;  // Data rozpoczęcia rezerwacji

    @Column(name = "end_date")
    private LocalDateTime endDate;    // Data zakończenia rezerwacji
}


