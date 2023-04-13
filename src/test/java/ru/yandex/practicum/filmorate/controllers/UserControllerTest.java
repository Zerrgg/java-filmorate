package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@JacksonStdImpl
class UserControllerTest {
    UserController controller;

    @BeforeEach
    void createController() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void AddUserWithProblemInTheLogin() {

        User user = new User("Z zerrgg", "name@mail.ru", LocalDate.of(1980, 1, 10));
        assertThrows(ValidationException.class, () -> controller.add(user));
    }

    @Test
    void AddUserWithoutName() throws ValidationException {

        User user = new User("Zzerrgg", "name@mail.ru", LocalDate.of(1985, 2, 20));
        user.setName(null);
        controller.add(user);
        assertEquals(1, controller.getAll().size(), "Пользователь не создан");
        assertEquals("Zzerrgg", controller.getAll().get(0).getName(), "Имя не совпадает с логином");
    }

    @Test
    void AddUser() throws ValidationException {

        User user = new User("Zzerrgg", "name@mail.ru", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.add(user);
        assertEquals("Дмитрий", controller.getAll().get(0).getName(), "Имя не совпадает");
    }

    @Test
    void UpdateUser() throws ValidationException {

        User user = new User("Zzerrgg", "name@mail.ru", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.add(user);
        User user2 = new User("Aarrgg", "name@mail.ru", LocalDate.of(1995, 4, 5));
        user2.setId(1);
        controller.update(user2);
        assertEquals("Aarrgg", controller.getAll().get(0).getLogin(), "Пользователь не обновлен");
    }

    @Test
    void UpdateUserNotUsingId() throws UserNotFoundException {

        User user = new User("Zzerrgg", "name@mail.ru", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.add(user);
        User user2 = new User("Bberrgen", "name@mail.ru", LocalDate.of(2000, 5, 15));
        assertThrows(UserNotFoundException.class, () -> controller.update(user2));
    }

    @Test
    void IncorrectUpdateUser() throws UserNotFoundException {

        User user = new User("Zzerrgg", "name@mail.ru", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.add(user);
        User user2 = new User("Bberrgen", "name@mail.ru", LocalDate.of(2005, 6, 25));
        user2.setId(100);
        assertThrows(UserNotFoundException.class, () -> controller.update(user2));
    }

}