package com.kodilla.library.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodilla.library.dto.FavoriteBookDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.FavoriteAlreadyExistsException;
import com.kodilla.library.exception.FavoriteNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.FavoriteBookMapper;
import com.kodilla.library.model.FavoriteBook;
import com.kodilla.library.service.FavoriteBookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;
    private final FavoriteBookMapper favoriteBookMapper;

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<FavoriteBookDTO>> getFavoritesByUser(@PathVariable Long idUser)
            throws UserNotFoundByIdException {
        List<FavoriteBook> favorites = favoriteBookService.getFavoritesByUser(idUser);
        return ResponseEntity.ok(favoriteBookMapper.toDtoList(favorites));
    }

    @PostMapping
    public ResponseEntity<FavoriteBookDTO> addFavoriteBook(
            @RequestParam Long idUser,
            @RequestParam Long idBook)
            throws UserNotFoundByIdException, BookNotFoundByIdException, FavoriteAlreadyExistsException {

        FavoriteBook favorite = favoriteBookService.addFavoriteBook(idUser, idBook);
        return ResponseEntity.ok(favoriteBookMapper.toDto(favorite));
    }

    @DeleteMapping
    public ResponseEntity<String> removeFavoriteBook(
            @RequestParam Long idUser,
            @RequestParam Long idBook)
            throws FavoriteNotFoundException {

        favoriteBookService.removeFavoriteBook(idUser, idBook);
        return ResponseEntity.ok("Favorite has been removed");
    }
}
