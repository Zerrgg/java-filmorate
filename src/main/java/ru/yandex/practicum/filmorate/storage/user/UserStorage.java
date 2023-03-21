package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User add(User user);

    User get(long id);

    List<User> getAll();

    List<User> getFriends(long id);

    User update(User user);

}