package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.service.FileService;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.GenreService;


@ThreadSafe
@Controller
@RequestMapping("/films") /* Работать с кандидатами будем по URI /films/** */
public class FilmController {

    private final FilmService filmService;
    private final GenreService genreService;

    public FilmController(FilmService filmService, GenreService genreService) {
        this.filmService = filmService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("DTOfilms", filmService.findAll());
        return "films/list";
    }
}
