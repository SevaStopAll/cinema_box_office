package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.service.FileService;
import ru.job4j.cinema.service.FilmService;


@Controller
@RequestMapping("/films") /* Работать с кандидатами будем по URI /films/** */
public class FilmController {

    private final FilmService filmService;
    private final FileService fileService;

    public FilmController(FilmService filmService, FileService fileService) {
        this.fileService = fileService;
        this.filmService = filmService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/list";
    }

}
