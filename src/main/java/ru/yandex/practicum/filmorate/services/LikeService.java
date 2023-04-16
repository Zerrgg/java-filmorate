package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;
    private final UserDao userDao;
    private final FilmDao filmDao;

    private void check(long filmId, long userId) {
        filmDao.get(filmId);
        userDao.get(userId);
    }

    public void add(long filmId, long userId) {
        check(filmId, userId);
        likeDao.add(userId, filmId);
    }

    public void delete(long filmId, long userId) {
        check(filmId, userId);
        likeDao.delete(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return likeDao.getPopularFilms(count);
    }

}