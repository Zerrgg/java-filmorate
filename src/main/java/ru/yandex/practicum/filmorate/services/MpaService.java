package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa get(int id) {
        return mpaDao.get(id);
    }

    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }
}