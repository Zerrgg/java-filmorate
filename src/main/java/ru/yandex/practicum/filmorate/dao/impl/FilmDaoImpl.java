package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
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
    private final DirectorDao directorDao;

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
        List<Director> filmDirectors = directorDao.addFilm(film.getId(), film.getDirectors());
        film.setDirectors(filmDirectors);
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
        return jdbcTemplate.query(sql, new FilmMapper(mpaDao, genreDao, directorDao), filmId).stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Фильм не найден"));
    }

    @Override
    public List<Film> getDirectorFilms(int directorId, String sortBy) {
        String sql;
        switch (sortBy) {
            case "year":
                sql = "SELECT*\n" +
                        "FROM film_director fd\n" +
                        "JOIN films f ON fd.film_id=f.film_id\n" +
                        "WHERE fd.director_id=?\n" +
                        "ORDER BY f.release_date";
                break;

            case "likes":
                sql = "SELECT*\n" +
                        "FROM film_director fd\n" +
                        "JOIN films f ON fd.film_id=f.film_id\n" +
                        "LEFT JOIN movie_likes ml ON fd.film_id=ml.film_id\n" +
                        "WHERE fd.director_id=?\n" +
                        "GROUP BY fd.film_id, ml.user_id\n" +
                        "ORDER BY COUNT (ml.user_id) DESC";
                break;

            default:
                throw new ValidationException("Некорректный параметр сортировки");
        }
        return jdbcTemplate.query(sql, filmMapper, directorId);
    }
}