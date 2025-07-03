package com.kodilla.library.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
    private Long idBookStatistics;

    @OneToOne
    @JoinColumn(name = "idBook")
    private Book book;

    @Builder.Default
    private Long loanCount = 1L;

    @Builder.Default
    private Long reservationCount = 1L;

    @Builder.Default
    private Double averageRating = 0.0;
}

