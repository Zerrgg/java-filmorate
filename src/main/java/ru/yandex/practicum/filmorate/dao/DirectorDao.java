package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> addDirectorInFilm(long filmId, List<Director> directors);

    void deleteFromFilm(long filmId);

    Director get(int id);

    List<Director> getAll();

    Director update(Director director);


    List<Director> getDirectorListFromFilm(long filmId);

    Director add(Director director);

    void delete(int id);

}
