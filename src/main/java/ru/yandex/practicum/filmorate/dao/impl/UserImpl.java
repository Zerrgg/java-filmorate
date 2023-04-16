package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserImpl implements UserDao {

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
        validator(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        validator(user);
        check(user);
        if (!isExist(user.getId())) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        String sql = "UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=? ";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User get(long userId) {
        if (!isExist(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        String sql = "SELECT user_id, user_name, login, email, birthday FROM users WHERE user_id=?";
        return jdbcTemplate.queryForObject(sql, userMapper, userId);
    }

    private void check(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Не был указан пользователь в запросе");
            user.setName(user.getLogin());
        }
    }

    private void validator(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Пользователь при создании логина использовал символы пробела");
            throw new ValidationException("Логин содержит символы пробела");
        }
    }

    private boolean isExist(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        return userRows.next();
    }

}