package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.FavoriteBookDTO;
import com.kodilla.library.model.Book;
import com.kodilla.library.model.FavoriteBook;
import com.kodilla.library.model.User;

@Component
public class FavoriteBookMapper {

    public FavoriteBookDTO toDto(FavoriteBook favorite) {
        if (favorite == null) {
            return null;
        }
        return new FavoriteBookDTO(
                favorite.getIdFavoriteBook(),
                favorite.getUser().getIdUser(),
                favorite.getBook().getIdBook(),
                favorite.getAddedAt()
        );
    }

    public FavoriteBook toEntity(FavoriteBookDTO dto, User user, Book book) {
        if (dto == null) {
            return null;
        }
        return FavoriteBook.builder()
                .idFavoriteBook(dto.idFavoriteBook())
                .user(user)
                .book(book)
                .addedAt(dto.addedAt())
                .build();
    }

    public List<FavoriteBookDTO> toDtoList(List<FavoriteBook> favorites) {
        return favorites.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
