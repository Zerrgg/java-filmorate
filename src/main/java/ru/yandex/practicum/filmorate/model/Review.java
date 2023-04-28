package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Review {
    private long Id;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;
}
