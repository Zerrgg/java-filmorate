package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
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
        log.info("GET запрос на получение фильма");
        return filmService.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма: {}", film);
        return filmService.update(film);
    }

}
