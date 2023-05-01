package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Operations;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeDao likeDao;
    private final FeedDao feedDao;
    private static final EventTypes EVENT_TYPE = EventTypes.LIKE;
    private final FilmDao filmDao;
    private final UserDao userDao;

    public void add(long filmId, long userId) {
        filmDao.get(filmId);
        userDao.get(userId);
        likeDao.add(userId, filmId);
        feedDao.add(userId, filmId, EVENT_TYPE, Operations.ADD);
    }

    public void delete(long filmId, long userId) {
        filmDao.get(filmId);
        userDao.get(userId);
        likeDao.delete(filmId, userId);
        feedDao.add(userId, filmId, EVENT_TYPE, Operations.REMOVE);
    }
}