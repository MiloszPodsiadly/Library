package com.kodilla.library.dto;

import java.util.Set;

import com.kodilla.library.model.BookStatus;
public record BookDTO(
        Long idBook,
        String title,
        String author,
        String isbn,
        Boolean available,
        Set<BookStatus> statuses
) {}

