package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;

@Repository
@RequiredArgsConstructor
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(long filmId, long userId) {
        String sql = "MERGE INTO movie_likes(user_id, film_id) VALUES(?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(long filmId, long userId) {
        String sql = "DELETE FROM movie_likes\n" +
                "WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, filmId);
    }

}
