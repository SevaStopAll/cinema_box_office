package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<Film> findAll();
}
