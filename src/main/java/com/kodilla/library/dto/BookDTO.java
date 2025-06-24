package com.kodilla.library.dto;
import com.kodilla.library.model.BookStatus;
import java.util.Set;
public record BookDTO(
        Long idBook,
        String title,
        String author,
        String isbn,
        Boolean available,
        Set<BookStatus> statuses
) {}

