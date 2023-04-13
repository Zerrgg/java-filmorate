package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getByID(int mpaId) {
        String sql = "SELECT* FROM mpa WHERE mpa_id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Mpa.class), mpaId).stream().findFirst().orElseThrow(() -> new MpaNotFoundException("Рейтинг не найден"));
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT* FROM mpa";
        return new ArrayList<>(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Mpa.class)));
    }
}