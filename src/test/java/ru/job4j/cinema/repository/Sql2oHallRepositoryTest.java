package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository sql2oHallRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }


    @AfterEach
    public void clearHalls() {
        var halls = sql2oHallRepository.findAll();
        for (var hall : halls) {
            sql2oHallRepository.deleteById(hall.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var hall = sql2oHallRepository.save(new Hall(1, "test1", 10, 10, "test1"));
        var savedHall = sql2oHallRepository.findById(hall.getId()).get();
        assertThat(savedHall).usingRecursiveComparison().isEqualTo(hall);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var hall1 = sql2oHallRepository.save(new Hall(1, "test1", 10, 10, "test1"));
        var hall2 = sql2oHallRepository.save(new Hall(2, "test2", 20, 20, "test2"));
        var hall3 = sql2oHallRepository.save(new Hall(3, "test3", 30, 30, "test3"));
        var result = sql2oHallRepository.findAll();
        assertThat(result).isEqualTo(List.of(hall1, hall2, hall3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oHallRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oHallRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var hall = sql2oHallRepository.save(new Hall(1, "test1", 10, 10, "test1"));
        var isDeleted = sql2oHallRepository.deleteById(hall.getId());
        var savedHall = sql2oHallRepository.findById(hall.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedHall).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oHallRepository.deleteById(0)).isFalse();
    }
}