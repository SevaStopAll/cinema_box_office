package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> save(Ticket ticket);

    Optional<Ticket> findBySessionIdAndRowNumberAndPlaceNumber(int sessionId, int rowNumber, int placeNumber);

    Collection<Ticket> findAll();

    boolean deleteById(int id);

    Optional<Ticket> findById(int id);

}
