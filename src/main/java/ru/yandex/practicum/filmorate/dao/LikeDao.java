package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {

    List<Film> getPopularFilms(int count);

    void add(long filmId, long userId);

    void delete(long filmId, long userId);
}
