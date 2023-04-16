package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Qualifier
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> getAll() {
        return userDao.getAll();
    }


    public User add(User user) {
        return userDao.add(user);
    }


    public User update(User user) {
        return userDao.update(user);
    }


    public User get(long id) {
        return userDao.get(id);
    }

}