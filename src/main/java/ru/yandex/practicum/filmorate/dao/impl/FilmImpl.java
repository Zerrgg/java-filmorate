package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmImpl implements FilmDao {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmMapper filmMapper;


    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, filmMapper);
    }


    @Override
    public Film add(Film film) {
        validator(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        validator(film);
        if (!isExist(film.getId())) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        String sql = "UPDATE films SET film_title=?, description=?, duration=?, release_date=?,mpa_id=? WHERE film_id=? ";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        genreDao.delete(film.getId());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        return film;
    }

    @Override
    public Film get(long filmId) {
        String sql = "SELECT* FROM films WHERE film_id=?";
        return jdbcTemplate.query(sql, new FilmMapper(mpaDao, genreDao), filmId).stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Фильм не найден"));
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Ошибка в дате релиза");
            throw new ValidationException("Ошибка в дате релиза фильма");
        }
    }

//    private void updateGenres(Film film) {
//        List<Genre> genres = film.getGenres();
//        if (genres != null) {
//            String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES (?,?)";
//            new HashSet<>(genres).forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
//        }
//    }

    private boolean isExist(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", id);
        return userRows.next();
    }
}