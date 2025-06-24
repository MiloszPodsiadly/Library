package com.kodilla.library.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Table;


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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_statuses", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    private Set<BookStatus> statuses = new HashSet<>(Set.of(BookStatus.AVAILABLE));

}
