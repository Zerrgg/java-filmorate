package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaDao mpaDao;

    @GetMapping
    public List<Mpa> getAll() {
        log.info("GET запрос на получение всех рейтингов");
        return mpaDao.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getByID(@PathVariable int mpaId) {
        log.info("GET запрос на получение рейтинга");
        return mpaDao.getByID(mpaId);
    }
}