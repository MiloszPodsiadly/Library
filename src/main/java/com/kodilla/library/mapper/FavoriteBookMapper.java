package com.kodilla.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kodilla.library.dto.FavoriteBookDTO;
import com.kodilla.library.model.FavoriteBook;

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

    public List<FavoriteBookDTO> toDtoList(List<FavoriteBook> favorites) {
        return favorites.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
