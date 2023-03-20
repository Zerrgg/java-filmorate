package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate BOUNDARY_DATE = LocalDate.of(1895, 12, 25);
    private final Map<Long, Film> filmHashMap = new HashMap<>();
    private long id;

    private long idGenerator() {
        return ++id;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(BOUNDARY_DATE)) {
            log.warn("Внесена некорректная дата");
            throw new ValidationException("Ошибка в дате релиза фильма");
        }
    }

    private void checkId(long id) {
        if (!filmHashMap.containsKey(id)) {
            log.info("Нет фильма с таким id");
            throw new FilmNotFoundException("Фильм с таким id не найден");
        }
    }

    @Override
    public Film add(Film film) {
        validator(film);
        film.setId(idGenerator());
        filmHashMap.put(film.getId(), film);
        log.info("Запрос на добавление фильма успешно обработан");
        return film;
    }

    @Override
    public Film get(long id) {
        checkId(id);
        return filmHashMap.get(id);
    }

    @Override
    public List<Film> getAll() {
        if (!filmHashMap.isEmpty()) {
            return new ArrayList<>(filmHashMap.values());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть больше 0");
        }
        if (filmHashMap.isEmpty()) {
            log.info("GET запрос на популярные фильмы обработан. Получен пустой список");
            return Collections.emptyList();
        } else {
            log.info("GET запрос на популярные фильмы обработан.");
            return getAll().stream()
                    .sorted((s1, s2) -> Integer.compare(s2.getLikes(), s1.getLikes()))
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Film update(Film film) {
        validator(film);
        checkId(film.getId());
        Film updatedFilm = filmHashMap.get(film.getId());
        int updatedFilmLikes = updatedFilm.getLikes();
        film.setLikes(updatedFilmLikes);
        Set<Long> updatedFilmLikedUsersId = updatedFilm.getUsersIdWhoLiked();
        film.setUsersIdWhoLiked(updatedFilmLikedUsersId);
        filmHashMap.put(film.getId(), film);
        log.info("Запрос на обновление фильма успешно обработан");
        return film;
    }

}