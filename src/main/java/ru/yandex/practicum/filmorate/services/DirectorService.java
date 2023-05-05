package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorDao directorDao;

    public Director get(int id) {
        if (id <= 0) {
            log.info("Ошибка. id не должно быть нулевым или иметь отрицательное значение");
            throw new ObjectNotFoundException("Режиссёр не найден");
        }
        return directorDao.get(id);
    }

    public List<Director> getAll() {
        return directorDao.getAll();
    }

    public Director add(Director director) {
        return directorDao.add(director);
    }

    public Director update(Director director) {
        directorDao.get(director.getId());
        return directorDao.update(director);
    }

    public void delete(int id) {
        directorDao.delete(id);
    }
}
