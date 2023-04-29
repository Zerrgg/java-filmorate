package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Override
    public Mpa get(int mpaId) {
        try {
            String sql = "SELECT*\n" +
                    "FROM mpa\n" +
                    "WHERE mpa_id=?";
            return jdbcTemplate.queryForObject(sql, mpaMapper, mpaId);
        } catch (RuntimeException e) {
            log.info("Рейтинга с id - {} не существует в базе", mpaId);
            throw new ObjectNotFoundException("Рейтинг не найден");
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT*\n" +
                "FROM mpa";
        return jdbcTemplate.query(sql, mpaMapper);
    }
}