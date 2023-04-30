package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма: {}", film);
        return filmService.add(film);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.info("GET запрос на получение фильма с id: {}", id);
        return filmService.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма: {}", film);
        return filmService.update(film);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilms(@Valid @PathVariable int directorId, @RequestParam String sortBy) {
        log.info("GET запрос на получение фильмов снятых режиссёром с id: {}. И отсортированные по {}", directorId, sortBy);
        return filmService.getDirectorFilms(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(
            @RequestParam(value = "query", defaultValue = "unknown") String query,
            @RequestParam(value = "by", defaultValue = "unknown") String by) {
        log.info("GET запрос на получение списка " +
                "популярных фильмов расширенного поиска параметрами: QUERY = {}, BY = {}.", query, by);
        return filmService.searchFilms(query, by);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable long filmId) {
        log.info("DELETE запрос на удаление фильма с id: {}", filmId);
        filmService.delete(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularsFilms(
            @Positive @RequestParam(value = "count", defaultValue = "10") Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) LocalDate year) {
        log.info("GET запрос на просмотр популярных фильмов");
        return filmService.getPopularsFilms(count, genreId, year);
    }
}
