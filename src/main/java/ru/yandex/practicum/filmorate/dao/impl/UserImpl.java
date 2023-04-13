package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        String sql = "SELECT* FROM users";
        return new ArrayList<>(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class)));
    }

    @Override
    public User add(User user) {
        validator(user);
        check(user);
        String firstSqlRequest = "INSERT INTO users(user_name, login, email, birthday) VALUES (?,?,?,?)";
        jdbcTemplate.update(firstSqlRequest, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        String secondSqlRequest = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
        long userId = jdbcTemplate.queryForObject(secondSqlRequest, long.class);
        return get(userId);
    }

    @Override
    public User update(User user) {
        validator(user);
        check(user);
        String sql = "UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=? ";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getUserId());
        return get(user.getUserId());
    }

    @Override
    public User get(long userId) {
        String sql = "SELECT user_id, user_name, login, email, birthday FROM users WHERE user_id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userId).stream().findAny()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> getFriend(long userId) {
        get(userId);
        String sql = "SELECT user_id, user_name, login, email, birthday FROM users AS u" +
                "LEFT JOIN friendship AS f ON u.user_id=f.user_id_whom_request_was_sent WHERE f.user_id_who_sent_request=?";
        return new ArrayList<>(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userId));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        get(userId);
        get(friendId);
        String sql = "INSERT INTO friendship(user_id_who_sent_request, user_id_whom_request_was_sent) VALUES (?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        get(userId);
        get(friendId);
        String sql = "DELETE FROM friendship WHERE user_id_who_sent_request=? AND user_id_whom_request_was_sent=? ";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getMutualUsersFriends(long userId, long otherUserId) {
        get(userId);
        get(otherUserId);
        String sql = "SELECT* FROM users WHERE user_id IN(SELECT user_id_whom_request_was_sent" +
                "WHERE user_id_who_sent_request=? AND user_id_whom_request_was_sent IN" +
                "(SELECT user_id_whom_request_was_sent FROM friendship WHERE user_id_who_sent_request=?))";
        return new ArrayList<>(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userId, otherUserId));
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
}