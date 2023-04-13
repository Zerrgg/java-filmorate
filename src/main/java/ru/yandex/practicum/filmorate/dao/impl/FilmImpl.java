package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmImpl implements FilmDao {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final UserDao userDao;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT* FROM films";
        return new ArrayList<>(jdbcTemplate.query(sql, new FilmMapper(mpaDao, jdbcTemplate)));
    }

    @Override
    public Film add(Film film) {
        validator(film);
        String firstSqlRequest = "INSERT INTO films(film_title, description, duration, release_date, mpa_id) VALUES (?,?,?,?,?)";
        String secondSqlRequest = "SELECT film_id FROM films ORDER BY film_id DESC LIMIT 1";
        jdbcTemplate.update(firstSqlRequest, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getMpaId());
        long filmId = jdbcTemplate.queryForObject(secondSqlRequest, long.class);
        film.setFilmId(filmId);
        updateGenres(film);
        return get(filmId);

    }

    @Override
    public Film update(Film film) {
        validator(film);
        get(film.getFilmId());
        String firstSqlRequest = "UPDATE films SET film_title=?, description=?, duration=?, release_date=?,mpa_id=? WHERE film_id=? ";
        jdbcTemplate.update(firstSqlRequest, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getMpaId(), film.getFilmId());
        String secondSqlRequest = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(secondSqlRequest, film.getFilmId());
        updateGenres(film);
        return get(film.getFilmId());
    }

    @Override
    public Film get(long filmId) {
        String sql = "SELECT* FROM films WHERE film_id=?";
        return jdbcTemplate.query(sql, new FilmMapper(mpaDao, jdbcTemplate), filmId).stream().findFirst()
                .orElseThrow(() -> new FilmNotFoundException("Фильм не найден"));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Ошибка валидации");
        }
        String sql = "SELECT film_id, film_title, description, duration, release_date, mpa_id, count FROM (SELECT film_id ,COUNT(user_id) AS" +
                " count FROM movie_likes AS ml GROUP BY film_id) AS likes RIGHT JOIN films AS f ON f.film_id=likes.film_id" +
                " ORDER BY count DESC LIMIT ?";
        return new ArrayList<>(jdbcTemplate.query(sql, new FilmMapper(mpaDao, jdbcTemplate), count));
    }

    @Override
    public void addLike(long filmId, long userId) {
        get(filmId);
        userDao.get(userId);
        String sql = "INSERT INTO movie_likes(user_id, film_id) VALUES(?,?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        userDao.get(userId);
        get(filmId);
        String sql = "DELETE FROM movie_likes WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
        return get(filmId);
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Ошибка в дате релиза");
            throw new ValidationException("Ошибка в дате релиза фильма");
        }
    }

    private void updateGenres(Film film) {
        List<Genre> genres = film.getGenres();
        if (genres != null) {
            String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES (?,?)";
            new HashSet<>(genres).forEach(genre -> jdbcTemplate.update(sql, film.getFilmId(), genre.getGenreId()));
        }
    }
}