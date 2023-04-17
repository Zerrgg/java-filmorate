package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Service
@Qualifier
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    public List<User> getAll() {
        return userDao.getAll();
    }

    public User add(User user) {
        validator(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return userDao.add(user);
    }

    public User update(User user) {
        validator(user);
        check(user);
        if (!isExist(user.getId())) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return userDao.update(user);
    }

    public User get(long id) {
        if (!isExist(id)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        return userDao.get(id);
    }

    private boolean isExist(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        return userRows.next();
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

}