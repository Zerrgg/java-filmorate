package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class User {

    private long id;
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Past
    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        if (name == null || name.isBlank()) {
            name = login;
        }
        values.put("user_name", name);
        values.put("birthday", birthday);
        return values;
    }

}
