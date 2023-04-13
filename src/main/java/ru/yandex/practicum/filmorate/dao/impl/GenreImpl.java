package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getByID(int genreId) {
        String sql = "SELECT* FROM genres WHERE genre_id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class),
                genreId).stream().findFirst().orElseThrow(() -> new GenreNotFoundException("Жанр не найден"));
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT* FROM genres";
        return new ArrayList<>(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class)));
    }
}