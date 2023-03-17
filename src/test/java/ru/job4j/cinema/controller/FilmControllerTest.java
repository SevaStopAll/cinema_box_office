package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.GenreService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmControllerTest {

    private FilmService filmService;

    private GenreService genreService;

    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        genreService = mock(GenreService.class);
        filmController = new FilmController(filmService, genreService);
    }

    @Test
    public void whenRequestFilmListPageThenGetPageWithFilmsDTO() {
        var film1Dto = new FilmDto(1, "name1", "desc1", 1987, "genre1", 13, 90, 1);
        var film2Dto = new FilmDto(2, "name2", "desc2", 1987, "genre1", 18, 120, 1);
        var expectedFilmsDto = List.of(film1Dto, film2Dto);
        when(filmService.findAll()).thenReturn(expectedFilmsDto);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilmsDto = model.getAttribute("DTOfilms");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilmsDto).isEqualTo(expectedFilmsDto);
    }
}
