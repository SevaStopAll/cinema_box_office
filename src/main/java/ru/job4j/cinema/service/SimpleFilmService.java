package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final FileService fileService;

    private final GenreService genreService;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, FileService fileService, GenreService genreService) {
        this.filmRepository = sql2oFilmRepository;
        this.fileService = fileService;
        this.genreService = genreService;
    }

    @Override
    public Film save(Film film, FileDto image) {
        saveNewFile(film, image);
        return filmRepository.save(film);
    }

    private void saveNewFile(Film film, FileDto image) {
        var file = fileService.save(image);
        film.setFileId(file.getId());
    }

    @Override
    public Optional<Film> findById(int id) {
        return filmRepository.findById(id);
    }

    private String getGenreNameById(int id) {
        return genreService.findById(id).get().getName();
    }

    @Override
    public Collection<FilmDto> findAll() {
        List<Film> films = (List<Film>) filmRepository.findAll();
        List<FilmDto> listOfFilmDTO = new ArrayList<>();
        for (int i = 0; i < films.size(); i++) {
            listOfFilmDTO.add(new FilmDto(films.get(i).getId(), films.get(i).getName(), films.get(i).getDescription(), films.get(i).getYear(),
                    getGenreNameById(films.get(i).getGenreId()), films.get(i).getMinimalAge(), films.get(i).getDurationInMinutes(),
                    films.get(i).getFileId()));
        }
        return listOfFilmDTO;
    }

}