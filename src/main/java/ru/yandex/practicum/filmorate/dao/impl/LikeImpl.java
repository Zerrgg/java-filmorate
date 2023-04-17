package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.film_id, film_title, description, duration, release_date, mpa_id, count\n" +
                "FROM (SELECT film_id ,COUNT(user_id) AS count\n" +
                "FROM movie_likes AS ml\n" +
                "GROUP BY film_id) AS likes RIGHT JOIN films AS f ON f.film_id=likes.film_id\n" +
                "ORDER BY count DESC LIMIT ?";
        return new ArrayList<>(jdbcTemplate.query(sql, new FilmMapper(mpaDao, genreDao), count));
    }

    @Override
    public void add(long filmId, long userId) {
        String sql = "INSERT INTO movie_likes(user_id, film_id) VALUES(?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(long filmId, long userId) {
        String sql = "DELETE FROM movie_likes\n" +
                "WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
    }

}
