package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
@Qualifier
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final FeedDao feedDao;

    public List<User> getAll() {
        return userDao.getAll();
    }

    public User add(User user) {
        validator(user);
        return userDao.add(user);
    }

    public User update(User user) {
        validator(user);
        check(user);
        userDao.get(user.getId());
        return userDao.update(user);
    }

    public User get(long id) {
        if (id <= 0) {
            log.info("Ошибка. id не должно быть нулевым или иметь отрицательное значение {}", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return userDao.get(id);
    }


    private void validator(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Пользователь при создании логина использовал символы пробела");
            throw new ValidationException("Логин содержит символы пробела");
        }
    }

    private void check(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Не был указан пользователь в запросе");
            user.setName(user.getLogin());
        }
    }

    public List<Event> getFeed(long id) {
        userDao.get(id);
        return feedDao.getFeed(id);
    }

    public List<Film> getRecommendationsForUser(long userId) {
        userDao.get(userId);
        return userDao.getRecommendations(userId);
    }

    public void delete(long userId) {
        userDao.get(userId);
        userDao.delete(userId);
    }

}