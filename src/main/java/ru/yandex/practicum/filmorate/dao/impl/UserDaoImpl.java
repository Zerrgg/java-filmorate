package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private final FilmMapper filmMapper;

    @Override
    public List<User> getAll() {
        String sql = "SELECT*\n" +
                "FROM users";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=?";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User get(long userId) {
        try {
            String sql = "SELECT*\n" +
                    "FROM users\n" +
                    "WHERE user_id=?";
            return jdbcTemplate.queryForObject(sql, userMapper, userId);
        } catch (RuntimeException e) {
            log.info("Некорректный id {}", userId);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<Film> getRecommendations(long userId) {
        try {
            String sql = "SELECT f.FILM_ID, f.FILM_TITLE, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID\n" +
                    "FROM (SELECT ml.user_id, ml.FILM_ID, ml2.USER_ID AS other_user \n" +
                    "      FROM MOVIE_LIKES AS ml\n" +
                    "      INNER JOIN MOVIE_LIKES ml2 ON ml.FILM_ID = ml2.FILM_ID \n" +
                    "      WHERE ml.user_id = ?\n" +
                    "      GROUP BY ml.user_id, ml.FILM_ID, ml2.USER_ID) AS TABLE_1\n" +
                    "INNER JOIN MOVIE_LIKES AS  ml3 ON TABLE_1.other_user = ml3.USER_ID\n" +
                    "INNER JOIN FILMS AS  f ON ml3.FILM_ID = f.FILM_ID \n" +
                    "WHERE TABLE_1.other_user != ? AND TABLE_1.FILM_ID != ml3.FILM_ID;";
            return jdbcTemplate.query(sql, filmMapper, userId, userId);
        } catch (RuntimeException e) {
            log.info("Некорректный id {}", userId);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void delete(long userId) {
        String sql = "DELETE FROM users\n" +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

}