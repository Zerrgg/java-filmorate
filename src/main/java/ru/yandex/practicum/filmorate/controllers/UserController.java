package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> usersHashMap = new HashMap<>();
    private int id;

    private int idGenerator() {
        return ++id;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("POST на создание пользователя: {}", user);
        validatorUser(user);
        checkUserName(user);
        user.setId(idGenerator());
        usersHashMap.put(user.getId(), user);
        log.info("POST запрос выполнен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("PUT на обновление пользователя: {}", user);
        validatorUser(user);
        if (!usersHashMap.containsKey(user.getId())) {
            log.error("Пользователь отсутствует в списке: {}", user);
            throw new ValidationException("несуществующий id");
        }
        checkUserName(user);
        usersHashMap.put(user.getId(), user);
        log.info("PUT запрос выполнен");
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("GET на получение списка пользователей");
        if (!usersHashMap.isEmpty()) {
            return new ArrayList<>(usersHashMap.values());
        }
        return Collections.emptyList();
    }

    private void validatorUser(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не должен содержать пробелы");
            throw new ValidationException("Логин содержит пробельные символы");
        }
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Не указано имя пользователя");
            user.setName(user.getLogin());
        }
    }

}
