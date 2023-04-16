package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendDao friendDao;

    public List<User> get(long id) {
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
}
