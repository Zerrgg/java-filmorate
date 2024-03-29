package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.LikeService;

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

    @DeleteMapping("/{id}/like/{userId}")
    public void delete(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка пользователя с id - {} к фильму с id - {}", userId, id);
        likeService.delete(id, userId);
    }

}
