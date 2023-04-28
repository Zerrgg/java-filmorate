package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Director {
    private int id;
    @NotBlank
    private String name;

    public Director() {
    }

    public Director(int id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("director_name", name);
        return values;
    }

}
