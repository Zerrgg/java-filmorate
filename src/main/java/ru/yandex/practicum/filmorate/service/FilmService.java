package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public void addLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId);
        if (!film.getUsersIdWhoLiked().contains(userId)) {
            int likes = film.getLikes();
            film.setLikes(likes + 1);
            film.getUsersIdWhoLiked().add(userId);
            log.info("PUT запрос на добавление лайка обработан");
        }
    }

    public Film get(long id) {
        return filmStorage.get(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film deleteLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId);
        int likes = film.getLikes() - 1;
        if (!film.getUsersIdWhoLiked().contains(userId)) {
            throw new UserNotFoundException("Пользователь не найден");
        } else {
            film.setLikes(likes);
            film.getUsersIdWhoLiked().remove(userId);
            log.info("DELETE запрос на удаление лайка обработан.");
        }
        return film;
    }

}
