package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

public interface SessionRepository {
    Session save(Session session);

    Collection<Session> findAll();

    Optional<Session> findById(int id);

    boolean deleteById(int id);
}
