package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeDao likeDao;
    private final FilmDao filmDao;
    private final UserDao userDao;

    public void add(long filmId, long userId) {
        filmDao.get(filmId);
        userDao.get(userId);
        likeDao.add(userId, filmId);
    }

    public void delete(long filmId, long userId) {
        filmDao.get(filmId);
        userDao.get(userId);
        likeDao.delete(filmId, userId);
    }
}