package com.kodilla.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "favorite_books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "book_id"})
})
public class FavoriteBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavoriteBook;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime addedAt;
}
