package com.kodilla.library.controller;

import com.kodilla.library.dto.FavoriteBookDTO;
import com.kodilla.library.exception.BookNotFoundByIdException;
import com.kodilla.library.exception.FavoriteAlreadyExistsException;
import com.kodilla.library.exception.FavoriteNotFoundException;
import com.kodilla.library.exception.UserNotFoundByIdException;
import com.kodilla.library.mapper.FavoriteBookMapper;
import com.kodilla.library.model.FavoriteBook;
import com.kodilla.library.service.FavoriteBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;
    private final FavoriteBookMapper favoriteBookMapper;

    // ✅ GET all favorites by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteBookDTO>> getFavoritesByUser(@PathVariable Long userId)
            throws UserNotFoundByIdException {
        List<FavoriteBook> favorites = favoriteBookService.getFavoritesByUser(userId);
        return ResponseEntity.ok(favoriteBookMapper.toDtoList(favorites));
    }

    // ✅ POST – add to favorites
    @PostMapping
    public ResponseEntity<FavoriteBookDTO> addFavoriteBook(
            @RequestParam Long userId,
            @RequestParam Long bookId)
            throws UserNotFoundByIdException, BookNotFoundByIdException, FavoriteAlreadyExistsException {

        FavoriteBook favorite = favoriteBookService.addFavoriteBook(userId, bookId);
        return ResponseEntity.ok(favoriteBookMapper.toDto(favorite));
    }

    // ✅ DELETE – remove from favorites
    @DeleteMapping
    public ResponseEntity<Void> removeFavoriteBook(
            @RequestParam Long userId,
            @RequestParam Long bookId)
            throws FavoriteNotFoundException {

        favoriteBookService.removeFavoriteBook(userId, bookId);
        return ResponseEntity.noContent().build();
    }
}
