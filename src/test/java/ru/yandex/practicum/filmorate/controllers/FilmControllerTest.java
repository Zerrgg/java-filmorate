package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@JacksonStdImpl
class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    void createController() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
    }

    @Test
    void AddFilmCorrectMinDate() throws ValidationException {

        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.add(film);
        assertEquals(1, controller.getAll().size(), "Фильм не добавлен");
    }

    @Test
    void AddFilmIncorrectDate() {

        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1880, 10, 10), 130);
        assertThrows(ValidationException.class, () -> controller.add(film));
    }

    @Test
    void CorrectUpdateFilm() throws ValidationException {

        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.add(film);
        Film film2 = new Film("Фильм2", "Описание фильма2", LocalDate.of(1895, 12, 30), 120);
        film2.setId(1);
        controller.update(film2);
        assertEquals("Фильм2", controller.getAll().get(0).getName(), "Фильм не обновлен");
    }

    @Test
    void UpdateFilmNotUsingId() throws ValidationException {

        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.add(film);
        Film film2 = new Film("Фильм", "Описание фильма", LocalDate.of(1999, 11, 10), 130);
        assertThrows(ValidationException.class, () -> controller.update(film2));
    }

    @Test
    void IncorrectUpdateFilm() throws ValidationException {

        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.add(film);
        Film film2 = new Film("Фильм", "Описание фильма", LocalDate.of(1900, 10, 10), 120);
        film2.setId(100);
        assertThrows(ValidationException.class, () -> controller.update(film2));
    }
}