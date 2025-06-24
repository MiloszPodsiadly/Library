package com.kodilla.library.model;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.time.LocalDateTime;
import jakarta.persistence.Table;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id_user")
    private Long idUser;

    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "email")
    private String email;


    @Getter
    @Column(name = "password_hash")
    protected String passwordHash;

    @Builder.Default
    @Setter
    @Getter
    @Column(name = "active")
    private Boolean active = false;

    @Setter
    @Getter
    @Column(name = "token")
    private String token;

    @Setter
    @Getter
    @Column(name = "token_created_at")
    private LocalDateTime tokenCreatedAt;

    @Setter
    @Getter
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    private List<Loan> loans;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    private List<Review> reviews;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    private List<Fine> fines;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Getter
    private List<FavoriteBook> favoriteBooks;
}
