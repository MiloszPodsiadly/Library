package com.kodilla.library.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    @Setter
    @Column(name = "title")
    private String title;

    @Setter
    @Column(name = "author")
    private String author;

    @Setter
    @Column(name = "isbn")
    private String isbn;

    @Builder.Default
    @Setter
    private Boolean available = true;

    @OneToMany(mappedBy = "book", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Loan> loans;

    @OneToMany(mappedBy = "book", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "book", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Review> reviews;

    @Builder.Default
    @ElementCollection(targetClass = BookStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "book_statuses",
            joinColumns = @JoinColumn(name = "book_id_book", referencedColumnName = "idBook")
    )
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Set<BookStatus> statuses = new HashSet<>(Set.of(BookStatus.AVAILABLE));
}
