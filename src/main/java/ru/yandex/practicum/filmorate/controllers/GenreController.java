package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreDao genreDao;

    @GetMapping
    public List<Genre> getAll() {
        log.info("GET запрос на получение всех жанров");
        return genreDao.getAll();
    }

    @GetMapping("/{id}")
    public Genre getByID(@PathVariable int genreId) {
        log.info("GET запрос на получение жанра");
        return genreDao.getByID(genreId);
    }
}