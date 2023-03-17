package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var optionalUser = sql2oUserRepository.save(new User(0, "ivan@mail.ru", "Ivan", "12345"));
        var user = optionalUser.get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTwoTimesSameUser() {
        User user = new User(0, "ivan@mail.ru", "Ivan", "12345");
        var savedUser = sql2oUserRepository.save(user);
        assertThat(savedUser.get()).usingRecursiveComparison().isEqualTo(user);
        var againSavedUser = sql2oUserRepository.save(user);
        assertThat(againSavedUser).isEmpty();
    }


    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "ivan1@mail.ru", "Ivan1", "12345"));
        var user2 = sql2oUserRepository.save(new User(0, "ivan2@mail.ru", "Ivan2", "12345"));
        var user3 = sql2oUserRepository.save(new User(0, "ivan3@mail.ru", "Ivan3", "12345"));
        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1.get(), user2.get(), user3.get()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        var savedUser = sql2oUserRepository.findByEmailAndPassword("ivan1@mail.ru", "12345");
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var user = sql2oUserRepository.save(new User(0, "ivan1@mail.ru", "Ivan1", "12345"));
        var isDeleted = sql2oUserRepository.deleteById(user.get().getId());
        var savedUser = sql2oUserRepository.findByEmailAndPassword("ivan1@mail.ru", "12345");
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }
}