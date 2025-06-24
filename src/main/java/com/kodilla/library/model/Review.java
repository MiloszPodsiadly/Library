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
import jakarta.persistence.Table;



    @Entity
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "reviews")
    public class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idReview;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;


        private Integer rating; // 1–5

        private String comment;

        private LocalDateTime createdAt;
    }

