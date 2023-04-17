package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


public interface GenreDao {
    Genre get(int id);

    List<Genre> getAll();

    List<Genre> getGenresListForFilm(long filmId);

    List<Genre> add(long filmId, List<Genre> genres);

    void delete(long filmId);

}