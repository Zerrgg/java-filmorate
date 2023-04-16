package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Override
    public Mpa get(int mpaId) {
        if (!isExist(mpaId)) {
            throw new ObjectNotFoundException("рейтинг не найден");
        }
        String sql = "SELECT* FROM mpa WHERE mpa_id=?";
        return jdbcTemplate.queryForObject(sql, mpaMapper, mpaId);
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT* FROM mpa";
        return jdbcTemplate.query(sql, mpaMapper);
    }

    private boolean isExist(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?", id);
        return userRows.next();
    }
}