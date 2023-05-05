package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    @Override
    public Director get(int directorId) {
        try {
            String sql = "SELECT*\n" +
                    "FROM director WHERE director_id=?";
            return jdbcTemplate.queryForObject(sql, directorMapper, directorId);
        } catch (RuntimeException e) {
            log.info("Режиссёра с id - {} не существует в базе", directorId);
            throw new ObjectNotFoundException("Режиссёр не найден");
        }
    }

    @Override
    public List<Director> getAll() {
        String sql = "SELECT*\n" +
                "FROM director";
        return jdbcTemplate.query(sql, directorMapper);
    }

    @Override
    public List<Director> getDirectorListFromFilm(long filmId) {
        String sql = "SELECT fd.*, d.director_name\n" +
                "FROM film_director AS fd JOIN director AS d ON d.director_id = fd.director_id\n" +
                "WHERE fd.film_id = ?";
        return jdbcTemplate.query(sql, directorMapper, filmId);
    }

    @Override
    public List<Director> addDirectorInFilm(long filmId, List<Director> directors) {
        String sql = "MERGE INTO film_director (film_id, director_id) KEY(film_id, director_id) VALUES (?, ?)";
        if (directors == null || directors.isEmpty()) {
            log.info("Режиссёр ещё не был добавлен в базу.");
            return new ArrayList<>();
        }
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, directors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        });
        return getDirectorListFromFilm(filmId);
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE director SET director_name=? WHERE director_id=?";
        jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public Director add(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");
        director.setId((int) simpleJdbcInsert.executeAndReturnKey(directorMapper.toMap(director.getName())));
        return director;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM director\n" +
                "WHERE director_id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFromFilm(long filmId) {
        String sql = "DELETE FROM film_director\n" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

}
