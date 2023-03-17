package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.service.FileService;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.SessionService;

@ThreadSafe
@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final FilmService filmService;
    private final HallService hallService;


    public SessionController(SessionService sessionService, FilmService filmService, HallService hallService) {
        this.sessionService = sessionService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("DtoSessions", sessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = sessionService.findById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        var filmSession = filmSessionOptional.get();
        model.addAttribute("sessionId", filmSession.getId());
        model.addAttribute("rows", hallService.getRowCountByHallId(filmSession.getHallId()));
        model.addAttribute("places", hallService.getPlaceCountByHallId(filmSession.getHallId()));
        model.addAttribute("filmName", filmService.findById(filmSession.getFilmId()).get().getName());
        return "tickets/buy";
    }
}

