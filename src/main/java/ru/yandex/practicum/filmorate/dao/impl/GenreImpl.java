package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Genre get(int genreId) {
        String sql = "SELECT*\n" +
                "FROM genres WHERE genre_id=?";
        return jdbcTemplate.queryForObject(sql, genreMapper, genreId);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT*\n" +
                "FROM genres";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public void delete(long filmId) {
        String sql = "DELETE FROM film_genre\n" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Genre> getGenresListForFilm(long filmId) {
        String sql = "SELECT fg.*, g.genre_name\n" +
                "FROM film_genre AS fg JOIN genres AS g ON g.genre_id = fg.genre_id\n" +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }

    @Override
    public List<Genre> add(long filmId, List<Genre> genres) {
        String sql = "MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (?, ?)";
        if (genres == null || genres.isEmpty()) {
            return new ArrayList<>();
        }
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
        return getGenresListForFilm(filmId);
    }
}