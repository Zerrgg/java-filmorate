package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

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
        String sql = "UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=? ";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
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
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

}