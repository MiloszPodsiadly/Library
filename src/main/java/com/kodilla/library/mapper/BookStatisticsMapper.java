package com.kodilla.library.mapper;

import com.kodilla.library.dto.BookStatisticsDTO;
import com.kodilla.library.model.BookStatistics;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookStatisticsMapper {

    public BookStatisticsDTO toDto(BookStatistics stats) {
        if (stats == null) {
            return null;
        }
        return new BookStatisticsDTO(
                stats.getBook().getIdBook(),
                stats.getLoanCount(),
                stats.getReservationCount(),
                stats.getAverageRating()
        );
    }

    public List<BookStatisticsDTO> toDtoList(List<BookStatistics> statisticsList) {
        return statisticsList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
