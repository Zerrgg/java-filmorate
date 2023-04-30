package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {

    void add(long filmId, long userId);

    void delete(long filmId, long userId);
}
