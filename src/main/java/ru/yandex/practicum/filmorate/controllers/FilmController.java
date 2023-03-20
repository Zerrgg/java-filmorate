package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма: {}", film);
        return filmService.add(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT запрос на добавление лайка к фильму");
        filmService.addLike(id, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("films/{id}")
    public Film get(@PathVariable long id) {
        log.info("GET запрос на получение фильма");
        return filmService.get(id);
    }

    @GetMapping("films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("GET запрос на просмотр популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма: {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка");
        return filmService.deleteLike(id, userId);
    }

}
