package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    List<User> get(long userId);

    void add(long userId, long friendId);

    void delete(long userId, long friendId);

    List<User> getMutualUsersFriends(long userId, long otherUserId);
}
