package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> addFilm(long filmId, List<Director> directors);

    void deleteFromFilm(long filmId);

    Director get(int id);

    List<Director> getAll();

    Director update(Director director);


    List<Director> getDirectorListForFilm(long filmId);

    Director add(Director director);

    void delete(int id);

}
