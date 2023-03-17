package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;
    private static Sql2oSessionRepository sql2oFilmSessionRepository;
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oHallRepository sql2oHallRepository;
    private static File file;
    private static Genre genre;
    private static Film film;
    private static Hall hall;
    private static Session filmSession;
    private static LocalDateTime startTime = now().truncatedTo(ChronoUnit.MINUTES);
    private static LocalDateTime endTime = startTime.plusMinutes(90).truncatedTo(ChronoUnit.MINUTES);

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oSessionRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);

        file = new File("test", "test");
        sql2oFileRepository.save(file);

        genre = new Genre(1, "Триллер");
        sql2oGenreRepository.save(genre);

        film = new Film(1, "test", "test", 1, genre.getId(), 1, 1, file.getId());
        sql2oFilmRepository.save(film);

        hall = new Hall(1, "test1", 10, 10, "test1");
        sql2oHallRepository.save(hall);

        filmSession = new Session(1, film.getId(), hall.getId(), startTime, endTime, 300);
        sql2oFilmSessionRepository.save(filmSession);

    }

    @AfterAll
    public static void deleteFile() {
        sql2oFilmSessionRepository.deleteById(filmSession.getId());
        sql2oFilmRepository.deleteById(film.getId());
        sql2oFileRepository.deleteById(file.getId());
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oHallRepository.deleteById(hall.getId());
    }

    @AfterEach
    public void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var ticket = sql2oTicketRepository.save(new Ticket(1, filmSession.getId(), 1, 1, 1));
        var savedTicket = sql2oTicketRepository.findById(ticket.get().getId());
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(1, filmSession.getId(), 1, 1, 1));
        var ticket2 = sql2oTicketRepository.save(new Ticket(2, filmSession.getId(), 2, 2, 1));
        var ticket3 = sql2oTicketRepository.save(new Ticket(3, filmSession.getId(), 3, 3, 1));
        var result = sql2oTicketRepository.findAll();
        assertThat(result).isEqualTo(List.of(ticket1.get(), ticket2.get(), ticket3.get()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oTicketRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var ticket = sql2oTicketRepository.save(new Ticket(1, filmSession.getId(), 1, 1, 1));
        var isDeleted = sql2oTicketRepository.deleteById(ticket.get().getId());
        var savedTicket = sql2oTicketRepository.findById(ticket.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTicket).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0)).isFalse();
    }
}