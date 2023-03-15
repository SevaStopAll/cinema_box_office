package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;

public interface HallService {
    Collection<Hall> findAll();
}
