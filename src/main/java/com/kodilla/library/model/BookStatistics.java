package com.kodilla.library.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book_statistics")
public class BookStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    @OneToOne
    @JoinColumn(name = "idBook")
    private Book book;

    @Builder.Default
    private Long loanCount = 0L;

    @Builder.Default
    private Long reservationCount = 0L;

    @Builder.Default
    private Double averageRating = 0.0;
}

