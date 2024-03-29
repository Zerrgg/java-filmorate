package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmDao {
    List<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Film get(long filmId);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getFilmBySearch(String query, String by);

    List<Film> getDirectorFilms(int directorId, String sortBy);

    List<Film> getPopularsFilms(Integer count, Integer genreId, Integer year);

    void delete(long filmId);

}