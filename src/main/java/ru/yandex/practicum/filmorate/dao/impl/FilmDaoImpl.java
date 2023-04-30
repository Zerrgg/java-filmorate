package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        List<Director> filmDirectors = directorDao.addDirectorInFilm(film.getId(), film.getDirectors());
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
    public List<Film> getFilmBySearch(String query, String by) {
        StringBuilder sql = new StringBuilder("SELECT * "
                + "FROM films f "
                + "LEFT JOIN movie_likes ml ON f.film_id = ml.film_id "
                + "LEFT JOIN mpa m ON m.mpa_id = f.mpa_id "
                + "LEFT JOIN film_director fd ON f.film_id = fd.film_id "
                + "LEFT JOIN director d ON fd.director_id = d.director_id ");
        if (by.equals("title")) {
            sql.append("WHERE LOWER(f.film_title) LIKE LOWER('%").append(query).append("%') ");
        }
        if (by.equals("director")) {
            sql.append("WHERE LOWER(d.director_name) LIKE LOWER('%").append(query).append("%') ");
        }
        if (by.equals("title,director") || by.equals("director,title")) {
            sql.append("WHERE LOWER(f.film_title) LIKE LOWER('%").append(query).append("%') ");
            sql.append("OR LOWER(d.director_name) LIKE LOWER('%").append(query).append("%') ");
        }
        sql.append("GROUP BY f.film_id, ml.film_id " + "ORDER BY COUNT(ml.film_id) DESC");
        return jdbcTemplate.query(sql.toString(), filmMapper);
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
                log.info("Запрашиваемой сортировки не существует: {}", sortBy);
                throw new ValidationException("Некорректный параметр сортировки");
        }
        return jdbcTemplate.query(sql, filmMapper, directorId);
    }


    public List<Film> getCommonFilms(long userId, long friendId) {
        String sql = "SELECT fffu.FILM_ID, fffu.FILM_TITLE, fffu.DESCRIPTION, fffu.RELEASE_DATE," +
                "fffu.DURATION,fffu.MPA_ID, fffu.MPA_NAME, fg.GENRE_ID\n" +
                "FROM(SELECT *\n" +
                "FROM(\n" +
                "SELECT mpf.*, ml.USER_ID AS like_first_user \n" +
                "FROM\n" +
                "(SELECT mpfilm.*\n" +
                "FROM (SELECT f.*, m.mpa_name, COUNT(ml.user_id) AS likes\n" +
                "FROM FILMS AS f LEFT JOIN MOVIE_LIKES ML ON f.FILM_ID = ML.FILM_ID \n" +
                "JOIN MPA M ON f.MPA_ID = M.MPA_ID\n" +
                "GROUP BY f.FILM_ID\n" +
                "ORDER BY likes desc) AS mpfilm) AS mpf\n" +
                "LEFT JOIN MOVIE_LIKES ML ON mpf.FILM_ID=ml.FILM_ID\n" +
                "WHERE ml.USER_ID=?) AS favorite_films_first_user) AS fffu\n" +
                "LEFT JOIN MOVIE_LIKES ML ON fffu.FILM_ID=ml.FILM_ID\n" +
                "LEFT JOIN FRIENDSHIP FS ON fffu.like_first_user=fs.USER_ID_WHOM_REQUEST_WAS_SENT\n" +
                "LEFT JOIN FRIENDSHIP fs2 ON fffu.like_first_user=fs2.USER_ID_WHO_SENT_REQUEST\n" +
                "LEFT JOIN FILM_GENRE AS fg ON fffu.FILM_ID=fg.FILM_ID\n" +
                "WHERE ml.USER_ID=?\n" +
                "GROUP BY fffu.FILM_ID";
        return jdbcTemplate.query(sql, filmMapper, userId, friendId);
    }
}