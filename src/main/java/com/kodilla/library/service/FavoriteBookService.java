package com.kodilla.library.service;

import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.FavoriteAlreadyExistsException;
import com.kodilla.library.exception.FavoriteNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.FavoriteBook;
import com.kodilla.library.model.User;
import com.kodilla.library.repository.BookRepository;
import com.kodilla.library.repository.FavoriteBookRepository;
import com.kodilla.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<FavoriteBook> getFavoritesByUser(Long idUser) throws UserNotFoundByIdException {
        if (!userRepository.existsById(idUser)) {
            throw new UserNotFoundByIdException(idUser);
        }
        return favoriteBookRepository.findAllByUser_IdUser(idUser);
    }

    public FavoriteBook addFavoriteBook(Long idUser, Long idBook)
            throws UserNotFoundByIdException, BookNotFoundByIdException, FavoriteAlreadyExistsException {

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundByIdException(idUser));
        Book book = bookRepository.findById(idBook)
                .orElseThrow(() -> new BookNotFoundByIdException(idBook));

        boolean alreadyExists = favoriteBookRepository.existsByUser_IdUserAndBook_IdBook(idUser, idBook);
        if (alreadyExists) {
            throw new FavoriteAlreadyExistsException(idUser, idBook);
        }

        FavoriteBook favorite = FavoriteBook.builder()
                .user(user)
                .book(book)
                .addedAt(LocalDateTime.now())
                .build();

        return favoriteBookRepository.save(favorite);
    }

    public void removeFavoriteBook(Long idUser, Long idBook)
            throws FavoriteNotFoundException {

        FavoriteBook favorite = favoriteBookRepository
                .findByUser_IdUserAndBook_IdBook(idUser, idBook)
                .orElseThrow(() -> new FavoriteNotFoundException(idUser, idBook));

        favoriteBookRepository.delete(favorite);
    }
}
