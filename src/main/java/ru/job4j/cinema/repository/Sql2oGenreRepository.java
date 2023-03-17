package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oGenreRepository implements GenreRepository {


    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Genre save(Genre genre) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO genres (name)
                    VALUES (:name)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", genre.getName());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            genre.setId(generatedId);
            return genre;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM genres WHERE id = :id");
            query.addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Genre> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres WHERE id = :id");
            query.addParameter("id", id);
            var vacancy = query.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(vacancy);
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM genres");
            return query.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetch(Genre.class);
        }
    }
}
