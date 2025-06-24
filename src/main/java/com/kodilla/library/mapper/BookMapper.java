package com.kodilla.library.mapper;

import com.kodilla.library.dto.BookDTO;
import com.kodilla.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDto(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDTO(
                book.getIdBook(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getAvailable(),
                book.getStatuses()
        );
    }

    public Book toEntity(BookDTO dto) {
        if (dto == null) {
            return null;
        }
        return Book.builder()
                .idBook(dto.idBook())
                .title(dto.title())
                .author(dto.author())
                .isbn(dto.isbn())
                .available(dto.available())
                .statuses(dto.statuses() != null ? dto.statuses() : Set.of())
                .build();
    }

    public List<BookDTO> toDtoList(List<Book> books) {
        return books.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
