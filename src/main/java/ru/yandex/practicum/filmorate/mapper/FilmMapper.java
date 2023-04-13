
package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private final MpaDao mpaDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getLong("film_id"));
        film.setName(rs.getString("film_title"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaDao.getByID(rs.getInt("mpa_id")));
        String sql = "SELECT g.genre_id, g.genre_name FROM genres AS g JOIN film_genre AS fg ON g.genre_id=fg.genre_id WHERE fg.film_id=?";
        List<Genre> genreList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), film.getFilmId());
        film.setGenres(genreList);
        return film;
    }
}