package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

public interface SessionService {

    Optional<Session> findById(int id);

    Collection<SessionDto> findAll();
}
