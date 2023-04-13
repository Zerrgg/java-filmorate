package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAll();

    User add(User user);

    User update(User user);

    User get(long userId);

    List<User> getFriend(long friendId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getMutualUsersFriends(long userId, long otherUserId);
}