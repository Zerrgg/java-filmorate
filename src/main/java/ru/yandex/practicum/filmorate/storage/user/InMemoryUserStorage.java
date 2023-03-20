package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int id;
    private final Map<Long, User> userHashMap = new HashMap<>();

    private int idGenerator() {
        return ++id;
    }

    private void validator(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Пользователь при создании логина использовал пробельные символы");
            throw new ValidationException("Логин содержит пробельные символы");
        }
    }

    private void checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пользователь не указал имя пользователя в запросе");
            user.setName(user.getLogin());
        }
    }

    private void checkUserId(long id) {
        if (!userHashMap.containsKey(id)) {
            log.info("Пользователь указал несуществующий id ");
            throw new UserNotFoundException("Пользователь с таким id не найден");
        }
    }
    @Override
    public User add(User user) {
        validator(user);
        checkUser(user);
        user.setId(idGenerator());
        userHashMap.put(user.getId(), user);
        log.info("POST запрос обработан");
        return user;
    }

    @Override
    public User get(long id) {
        checkUserId(id);
        log.info("GET запрос на получение пользователя обработан");
        return userHashMap.get(id);
    }

    @Override
    public List<User> getAll() {
        if (!userHashMap.isEmpty()) {
            return new ArrayList<>(userHashMap.values());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<User> getFriends(long id) {
        checkUserId(id);
        if (!userHashMap.get(id).getFriendsId().isEmpty()) {
            log.info("GET запрос на получение списка друзей пользователя обработан.");
            return userHashMap.get(id).getFriendsId().stream()
                    .map(userHashMap::get)
                    .collect(Collectors.toList());
        } else {
            log.info("GET запрос на получение списка друзей пользователя обработан. Список пуст");
            return Collections.emptyList();
        }
    }

    @Override
    public User update(User user) {
        validator(user);
        checkUserId(user.getId());
        User updatedUser = userHashMap.get(user.getId());
        Set<Long> updatedUserFriendsId = updatedUser.getFriendsId();
        user.setFriendsId(updatedUserFriendsId);
        userHashMap.put(user.getId(), user);
        log.info("PUT запрос обработан");
        return user;
    }

}