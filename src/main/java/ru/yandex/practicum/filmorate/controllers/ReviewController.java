package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.services.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        log.info("POST запрос на добавление отзыва: {}", review);
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("PUT запрос на обновление отзыва: {}", review);
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("DELETE запрос на удаление отзыва с id: {}", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable long id) {
        log.info("GET запрос на получение отзыва с id: {}", id);
        return reviewService.get(id);
    }

    @GetMapping()
    public List<Review> getByFilmReviews(@RequestParam(defaultValue = "0") Long filmId,
                                         @RequestParam(defaultValue = "10") int count) {
        log.info("GET запрос на получение отзывов фильма с id: {}", filmId);
        if (filmId == 0) {
            return reviewService.getAll();
        }
        return reviewService.getByFilmReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT запрос на добавление лайка от пользователя с id {} в отзыв с id: {}", id, userId);
        reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT запрос на добавление дизлайка от пользователя с id {} в отзыв с id: {}", id, userId);
        reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка от пользователя с id {} в отзыв с id: {}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление дизлайка от пользователя с id {} в отзыв с id: {}", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}
