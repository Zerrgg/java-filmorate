package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.LikeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/{id}/like/{userId}")
    public void add(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT запрос на добавление лайка пользователя с id - {} к фильму с id - {}", userId, id);
        likeService.add(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET запрос на просмотр популярных фильмов");
        return likeService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delete(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка пользователя с id - {} к фильму с id - {}", userId, id);
        likeService.delete(id, userId);
    }

}
