    package com.kodilla.library.model;

    import java.time.LocalDateTime;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.Table;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "reviews")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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

        private Integer rating;

        private String comment;

        private LocalDateTime createdAt;
    }
