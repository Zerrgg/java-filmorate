package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("film_title");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Mpa mpa = mpaDao.get(rs.getInt("mpa_id"));
        List<Genre> genres = genreDao.getGenresListForFilm(rs.getInt("film_id"));
        List<Director> directors = directorDao.getDirectorListFromFilm(rs.getInt("film_id"));

        return rs.wasNull() ? null : new Film(id, name, description, duration, releaseDate, mpa, genres, directors);
    }

}