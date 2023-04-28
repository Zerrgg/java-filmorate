package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorDao directorDao;

    public Director get(int id) {
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
        directorDao.get(id);
        directorDao.delete(id);
    }
}
