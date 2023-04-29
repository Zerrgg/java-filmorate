package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendDao friendDao;
    private final UserDao userDao;

    public List<User> get(long id) {
        userDao.get(id);
        return friendDao.get(id);
    }

    public void add(long userId, long userFriendId) {
        if (userFriendId <= 0 || userId <= 0) {
            log.warn("Некорректные данные: c userId = {} или userFriendI = {}", userId, userFriendId);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        friendDao.add(userId, userFriendId);
    }

    public void delete(long userId, long userFriendId) {
        userDao.get(userId);
        userDao.get(userFriendId);
        friendDao.delete(userId, userFriendId);
    }

    public List<User> getMutualUsersFriends(long userId, long otherUserId) {
        userDao.get(userId);
        userDao.get(otherUserId);
        return friendDao.getMutualUsersFriends(userId, otherUserId);
    }

}
