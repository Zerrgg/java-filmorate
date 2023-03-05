package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final Map<Integer, Film> filmsHashMap = new HashMap<>();
    private int id;

    private int idGenerator() {
        return ++id;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("POST на добавление фильма: {}", film);
        validatorFilm(film);
        film.setId(idGenerator());
        filmsHashMap.put(film.getId(), film);
        log.info("POST запрос выполнен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("PUT на обновление фильма: {}", film);
        validatorFilm(film);
        if (!filmsHashMap.containsKey(film.getId())) {
            log.error("Фильм отсутствует в списке: {}", film);
            throw new ValidationException("несуществующий id");
        }
        filmsHashMap.put(film.getId(), film);
        log.info("PUT запрос выполнен");
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET на получение списка фильмов");
        if (!filmsHashMap.isEmpty()) {
            return new ArrayList<>(filmsHashMap.values());
        }
        return Collections.emptyList();
    }

    private void validatorFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Дата релиза не может быть до: {}", BOUNDARY_DATE);
            throw new ValidationException("Ошибка по дате релиза");
        }
    }

}
