package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("POST запрос на создание пользователя");
        return userDao.add(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("PUT запрос на добавление в друзья");
        userDao.addFriend(userId, friendId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("GET запрос на получение списка всех пользователей");
        return userDao.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long userId) {
        log.info("GET запрос на получение пользователя");
        return userDao.get(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriend(@PathVariable long friendId) {
        log.info("GET запрос на получение списка друзей");
        return userDao.getFriend(friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualUsersFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.info("GET запрос на получение списка общих друзей");
        return userDao.getMutualUsersFriends(userId, otherUserId);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("PUT запрос на обновление пользователя");
        return userDao.update(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info("DELETE запрос на удаление из друзей");
        userDao.deleteFriend(userId, friendId);
    }

}
