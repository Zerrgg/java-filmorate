package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.services.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAll() {
        log.info("GET запрос на получение всех режиссёров");
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director get(@PathVariable int id) {
        log.info("GET запрос на получение режиссёра");
        return directorService.get(id);
    }

    @PostMapping
    public Director add(@Valid @RequestBody Director director) {
        log.info("POST запрос на добавление режиссёра: {}", director);
        return directorService.add(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("PUT запрос на обновление режиссёра: {}", director);
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("DELETE запрос на удаление режиссёра");
        directorService.delete(id);
    }
}
