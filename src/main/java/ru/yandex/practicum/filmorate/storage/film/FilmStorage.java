package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film get(long id);

    List<Film> getAll();

    List<Film> getPopularFilms(int count);

    Film update(Film film);

}