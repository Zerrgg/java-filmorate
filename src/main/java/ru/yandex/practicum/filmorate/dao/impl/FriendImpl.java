package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public List<User> get(long id) {
        String sql = "SELECT*\n" +
                "FROM users\n" +
                "WHERE user_id IN (SELECT user_id_whom_request_was_sent\n" +
                "FROM friendship\n" +
                "WHERE user_id_who_sent_request =?)";
        return jdbcTemplate.query(sql, userMapper, id);
    }

    @Override
    public void add(long userId, long friendId) {
        String sql = "INSERT INTO friendship(user_id_who_sent_request, user_id_whom_request_was_sent) VALUES (?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void delete(long userId, long friendId) {
        String sql = "DELETE FROM friendship\n" +
                "WHERE user_id_who_sent_request=? AND user_id_whom_request_was_sent=? ";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getMutualUsersFriends(long userId, long otherUserId) {
        String sql = "SELECT u.user_id, email, login, user_name, birthday\n" +
                "FROM friendship as f1\n" +
                "JOIN friendship as f2 ON  f2.user_id_whom_request_was_sent = f1.user_id_whom_request_was_sent AND f2.user_id_who_sent_request = ?\n" +
                "JOIN users u on u.user_id = f1.user_id_whom_request_was_sent\n" +
                "WHERE f1.user_id_who_sent_request = ?";
        List<User> resultList = jdbcTemplate.query(sql, userMapper, userId, otherUserId);
        if (resultList.isEmpty()) {
            return Collections.emptyList();
        }
        return resultList;
    }
}
