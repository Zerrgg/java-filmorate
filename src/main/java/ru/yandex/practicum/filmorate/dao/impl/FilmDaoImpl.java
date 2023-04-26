package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmMapper filmMapper;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT*\n" +
                "FROM films";
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET film_title=?, description=?, duration=?, release_date=?,mpa_id=?\n" +
                "WHERE film_id=? ";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film get(long filmId) {
        String sql = "SELECT*\n" +
                "FROM films\n" +
                "WHERE film_id=?";
        return jdbcTemplate.query(sql, new FilmMapper(mpaDao, genreDao), filmId).stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Фильм не найден"));
    }
}