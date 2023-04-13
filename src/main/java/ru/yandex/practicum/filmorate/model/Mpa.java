package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private int mpaId;
    private String mpa;

    public Mpa() {
    }

    public Mpa(int mpaId) {
        this.mpaId = mpaId;
    }
}