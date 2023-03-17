package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(int id);

    boolean deleteById(int id);

    Collection<Hall> findAll();

    Hall save(Hall hall);
}
