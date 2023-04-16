package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("user_id");
        String name = rs.getString("user_name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return rs.wasNull() ? null : new User(id, name, login, email, birthday);
    }

}