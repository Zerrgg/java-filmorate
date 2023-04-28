package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmDao {
    List<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Film get(long filmId);

    List<Film> getDirectorFilms(int directorId, String sortBy);

}