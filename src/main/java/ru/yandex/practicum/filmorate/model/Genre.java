package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private int genreId;
    private String genre;

    public Genre() {
    }

    public Genre(int genreId) {
        this.genreId = genreId;
    }
}