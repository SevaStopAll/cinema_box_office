package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.Collection;

public interface SessionService {
    Collection<Session> findAll();
}
