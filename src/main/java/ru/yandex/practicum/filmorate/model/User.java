package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {


    @NotBlank
    private final String login;
    @Email
    private final String email;
    @PastOrPresent
    private final LocalDate birthday;
    private int id;
    private String name;


}
