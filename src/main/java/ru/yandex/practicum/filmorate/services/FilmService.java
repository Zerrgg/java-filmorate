package ru.yandex.practicum.filmorate.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Qualifier
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    public List<Film> getAll() {
        return filmDao.getAll();
    }

    public Film add(Film film) {
        validator(film);
        return filmDao.add(film);
    }

    public Film update(Film film) {
        validator(film);
        filmDao.get(film.getId());
        genreDao.delete(film.getId());
        directorDao.deleteFromFilm(film.getId());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        List<Director> directors = directorDao.addDirectorInFilm(film.getId(), film.getDirectors());
        film.setDirectors(directors);
        return filmDao.update(film);
    }

    public Film get(long filmId) {
        if (filmId <= 0) {
            log.info("Ошибка. id не должно быть нулевым или иметь отрицательное значение {}", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        }
        return filmDao.get(filmId);
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Ошибка в дате релиза. Дата релиза должна быть после {}", BOUNDARY_DATE);
            throw new ValidationException("Ошибка в дате релиза фильма");
        }
    }

    public List<Film> getDirectorFilms(int directorId, String sortBy) {
        directorDao.get(directorId);
        return filmDao.getDirectorFilms(directorId, sortBy);
    }

}