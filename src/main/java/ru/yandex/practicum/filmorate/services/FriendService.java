package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendDao friendDao;
    private final JdbcTemplate jdbcTemplate;

    public List<User> get(long id) {
        if (!isExist(id)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return friendDao.get(id);
    }

    public void add(long userId, long userFriendId) {
        if (userFriendId <= 0 || userId <= 0) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        friendDao.add(userId, userFriendId);
    }

    public void delete(long userId, long userFriendId) {
        friendDao.delete(userId, userFriendId);
    }

    public List<User> getMutualUsersFriends(long userId, long otherUserId) {
        return friendDao.getMutualUsersFriends(userId, otherUserId);
    }

    private boolean isExist(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        return userRows.next();
    }

}
