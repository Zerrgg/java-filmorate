package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User add(User user);

    User update(User user);

    User get(long userId);

    List<Film>  getRecommendations(long id);

    void delete(long userId);
}