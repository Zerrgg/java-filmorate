package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FriendService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PutMapping("/{id}/friends/{friendId}")
    public void add(@PathVariable long id, @PathVariable long friendId) {
        log.info("PUT запрос на добавление в друзья пользователем с id - {}, пользователя с id - {}.", id, friendId);
        friendService.add(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> get(@PathVariable long id) {
        log.info("GET запрос на получение списка друзей пользователя с id: {}", id);
        return friendService.get(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualUsersFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("GET запрос на получение списка общих друзей пользователей с id: {}, {}", id, otherId);
        return friendService.getMutualUsersFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delete(@PathVariable long id, @PathVariable long friendId) {
        log.info("DELETE запрос пользователя с id - {} на удаление из списка друзей другого пользователя с id - {}", id, friendId);
        friendService.delete(id, friendId);
    }

}
