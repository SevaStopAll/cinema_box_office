package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    private static final Logger LOG = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES (:sessionID, :rowNumber, :placeNumber, :userId)           
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("sessionID", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());

            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        }  catch (Sql2oException e) {
            LOG.error(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public boolean update(Ticket ticket) {
        return false;
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Ticket> findAll() {
        return null;
    }
}
