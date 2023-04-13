package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {

    @NotBlank
    private String login;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;
    private long userId;
    private String name;


}
