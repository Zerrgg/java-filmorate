package ru.yandex.practicum.filmorate.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@Qualifier
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmDao;

    public List<Film> getAll() {
        return filmDao.getAll();
    }

    public Film add(Film film) {
        return filmDao.add(film);
    }

    public Film update(Film film) {
        return filmDao.update(film);
    }


    public Film get(long filmId) {
        return filmDao.get(filmId);
    }

}