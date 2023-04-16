package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;

    @Override
    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Ошибка валидации");
        }
        String sql = "SELECT f.film_id, film_title, description, duration, release_date, mpa_id, count FROM (SELECT film_id ,COUNT(user_id) AS" +
                " count FROM movie_likes AS ml GROUP BY film_id) AS likes RIGHT JOIN films AS f ON f.film_id=likes.film_id" +
                " ORDER BY count DESC LIMIT ?";
        return new ArrayList<>(jdbcTemplate.query(sql, new FilmMapper(mpaDao, genreDao), count));
    }

    @Override
    public void add(long filmId, long userId) {
        String sql = "INSERT INTO movie_likes(user_id, film_id) VALUES(?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(long filmId, long userId) {
        String sql = "DELETE FROM movie_likes WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
    }
}
