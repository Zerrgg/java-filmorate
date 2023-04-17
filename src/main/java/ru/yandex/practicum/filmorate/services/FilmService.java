package ru.yandex.practicum.filmorate.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Qualifier
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final JdbcTemplate jdbcTemplate;

    public List<Film> getAll() {
        return filmDao.getAll();
    }

    public Film add(Film film) {
        validator(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        return filmDao.add(film);
    }

    public Film update(Film film) {
        validator(film);
        if (!isExist(film.getId())) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        genreDao.delete(film.getId());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        return filmDao.update(film);
    }

    public Film get(long filmId) {
        return filmDao.get(filmId);
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Ошибка в дате релиза");
            throw new ValidationException("Ошибка в дате релиза фильма");
        }
    }

    private boolean isExist(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM films WHERE film_id = ?", id);
        return userRows.next();
    }

}