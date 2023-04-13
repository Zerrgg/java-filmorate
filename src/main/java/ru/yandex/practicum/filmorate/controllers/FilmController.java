package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmDao filmDao;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма: {}", film);
        return filmDao.add(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("PUT запрос на добавление лайка к фильму");
        filmDao.addLike(filmId, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return filmDao.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long filmId) {
        log.info("GET запрос на получение фильма");
        return filmDao.get(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET запрос на просмотр популярных фильмов");
        return filmDao.getPopularFilms(count);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма: {}", film);
        return filmDao.update(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка");
        filmDao.deleteLike(filmId, userId);
    }

}
