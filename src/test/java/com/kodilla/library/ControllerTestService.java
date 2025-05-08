package com.kodilla.library;

import com.kodilla.library.controller.ReservationController;
import com.kodilla.library.dto.ReservationDto;
import com.kodilla.library.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ControllerTestService {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void shouldCreateReservation() throws Exception {
        // Przygotowanie
        Long userId = 1L;
        Long bookId = 2L;

        ReservationDto reservationDto = new ReservationDto(1L, bookId, userId, LocalDate.now());

        when(reservationService.createReservation(any(), eq(bookId))).thenReturn(reservationDto);

        // Akcja i weryfikacja
        mockMvc.perform(post("/api/reservations/create")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.bookId", is(bookId.intValue())))
                .andExpect(jsonPath("$.userId", is(userId.intValue())));

        verify(reservationService, times(1)).createReservation(any(), eq(bookId));
    }

    @Test
    void shouldGetReservationsForBook() throws Exception {
        // Przygotowanie
        Long bookId = 10L;
        ReservationDto reservationDto = new ReservationDto(1L, bookId, 7L, LocalDate.now());
        List<ReservationDto> reservationList = List.of(reservationDto);

        when(reservationService.getReservationsForBook(bookId)).thenReturn(reservationList);

        // Akcja i weryfikacja
        mockMvc.perform(get("/api/reservations/book/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].bookId", is(bookId.intValue())))
                .andExpect(jsonPath("$[0].userId", is(7)));

        verify(reservationService, times(1)).getReservationsForBook(bookId);
    }

    @Test
    void shouldReturnEmptyListWhenNoReservationsForBook() throws Exception {
        // Przygotowanie
        Long bookId = 20L;

        when(reservationService.getReservationsForBook(bookId)).thenReturn(Collections.emptyList());

        // Akcja i weryfikacja
        mockMvc.perform(get("/api/reservations/book/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reservationService, times(1)).getReservationsForBook(bookId);
    }

    @Test
    void shouldGetUserReservations() throws Exception {
        // Przygotowanie
        Long userId = 5L;
        ReservationDto reservationDto = new ReservationDto(1L, 10L, userId, LocalDate.now());
        List<ReservationDto> reservationList = List.of(reservationDto);

        when(reservationService.getUserReservations(userId)).thenReturn(reservationList);

        // Akcja i weryfikacja
        mockMvc.perform(get("/api/reservations/user/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].bookId", is(10)))
                .andExpect(jsonPath("$[0].userId", is(userId.intValue())));

        verify(reservationService, times(1)).getUserReservations(userId);
    }

    @Test
    void shouldReturnEmptyListWhenNoReservationsForUser() throws Exception {
        // Przygotowanie
        Long userId = 99L;

        when(reservationService.getUserReservations(userId)).thenReturn(Collections.emptyList());

        // Akcja i weryfikacja
        mockMvc.perform(get("/api/reservations/user/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reservationService, times(1)).getUserReservations(userId);
    }
}
