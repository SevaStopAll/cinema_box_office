package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionControllerTest {

    private SessionService filmSessionService;

    private FilmService filmService;

    private SessionController filmSessionController;

    private HallService hallService;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(SessionService.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        filmSessionController = new SessionController(filmSessionService, filmService, hallService);
    }

    @Test
    public void whenRequestFilmSessionListPageThenGetPageWithFilmSessions() {
        var filmSession1DTO = new SessionDto(1, "Фильм1", "Зал1", LocalDateTime.now(), LocalDateTime.now());
        var filmSession2DTO = new SessionDto(1, "Фильм2", "Зал2", LocalDateTime.now(), LocalDateTime.now());
        var expectedFilmSessionsDTO = List.of(filmSession1DTO, filmSession2DTO);
        when(filmSessionService.findAll()).thenReturn(expectedFilmSessionsDTO);

        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessionsDTO = model.getAttribute("DtoSessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualFilmSessionsDTO).isEqualTo(expectedFilmSessionsDTO);
    }

    @Test
    public void whenRequestBuyTicketPageThenGetPageRowsAndPlacesToBuyTicket() {
        var optionalFilmSession = Optional.of(new Session(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 300));
        var optionalFilm = Optional.of(new Film(1, "foo", "bar", 1, 1, 1, 1, 1));
        var expectedRowList = List.of(1, 2, 3);
        var expectedPlaceList = List.of(1, 2, 3, 4, 5);
        when(filmSessionService.findById(anyInt())).thenReturn(optionalFilmSession);
        when(hallService.getRowCountByHallId(anyInt())).thenReturn(expectedRowList);
        when(hallService.getPlaceCountByHallId(anyInt())).thenReturn(expectedPlaceList);
        when(filmService.findById(anyInt())).thenReturn(optionalFilm);

        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var actualRows = model.getAttribute("rows");
        var actualPlaces = model.getAttribute("places");

        assertThat(view).isEqualTo("tickets/buy");
        assertThat(actualRows).isEqualTo(expectedRowList);
        assertThat(actualPlaces).isEqualTo(expectedPlaceList);
    }

    @Test
    public void whenRequestBuyTicketPageThenGetErrorPage() {
        when(filmSessionService.findById(anyInt())).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Сеанс с указанным идентификатором не найден");
    }
}
