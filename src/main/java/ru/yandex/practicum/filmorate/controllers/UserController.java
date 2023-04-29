package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("POST запрос на создание пользователя: {}", user);
        return userService.add(user);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("GET запрос на получение списка всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("GET запрос на получение пользователя с id: {}", id);
        return userService.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("PUT запрос на обновление пользователя: {}", user);
        return userService.update(user);
    }

}
