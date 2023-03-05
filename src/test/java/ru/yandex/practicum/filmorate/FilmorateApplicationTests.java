package ru.yandex.practicum.filmorate;


import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@JacksonStdImpl
class FilmorateApplicationTests {

    @Test
    void AddFilmCorrectMinDate() throws ValidationException {

        FilmController controller = new FilmController();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.addFilm(film);
        assertEquals("Фильм", controller.getFilms().get(0).getName(), "Фильм не добавлен");
    }

    @Test
    void AddFilmIncorrectDate() {

        FilmController controller = new FilmController();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1880, 10, 10), 130);
        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void CorrectUpdateFilm() throws ValidationException {

        FilmController controller = new FilmController();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.addFilm(film);
        Film film2 = new Film("Фильм2", "Описание фильма2", LocalDate.of(1895, 12, 30), 120);
        film2.setId(1);
        controller.updateFilm(film2);
        assertEquals("Фильм2", controller.getFilms().get(0).getName(), "Фильм не обновлен");
    }

    @Test
    void UpdateFilmNotUsingId() throws ValidationException {

        FilmController controller = new FilmController();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.addFilm(film);
        Film film2 = new Film("Фильм", "Описание фильма", LocalDate.of(1999, 11, 10), 130);
        assertThrows(ValidationException.class, () -> controller.updateFilm(film2));
    }

    @Test
    void IncorrectUpdateFilm() throws ValidationException {

        FilmController controller = new FilmController();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1895, 12, 30), 120);
        controller.addFilm(film);
        Film film2 = new Film("Фильм", "Описание фильма", LocalDate.of(1900, 10, 10), 120);
        film2.setId(100);
        assertThrows(ValidationException.class, () -> controller.updateFilm(film2));
    }

    @Test
    void AddUserWithProblemInTheLogin() {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Z zerrgg", LocalDate.of(1980, 1, 10));
        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    void AddUserWithoutName() throws ValidationException {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Zzerrgg", LocalDate.of(1985, 2, 20));
        user.setName(null);
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size(), "Пользователь не создан");
        assertEquals("Zzerrgg", controller.getUsers().get(0).getName(), "Имя не совпадает с логином");
    }

    @Test
    void AddUser() throws ValidationException {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Zzerrgg", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.addUser(user);
        assertEquals("Дмитрий", controller.getUsers().get(0).getName(), "Имя не совпадает");
    }

    @Test
    void UpdateUser() throws ValidationException {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Zzerrgg", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.addUser(user);
        User user2 = new User("name@mail.ru", "Aarrgg", LocalDate.of(1995, 4, 5));
        user2.setId(1);
        controller.updateUser(user2);
        assertEquals("Aarrgg", controller.getUsers().get(0).getLogin(), "Пользователь не обновлен");
    }

    @Test
    void UpdateUserNotUsingId() throws ValidationException {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Zzerrgg", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.addUser(user);
        User user2 = new User("name@mail.ru", "Bberrgen", LocalDate.of(2000, 5, 15));
        assertThrows(ValidationException.class, () -> controller.updateUser(user2));
    }

    @Test
    void IncorrectUpdateUser() throws ValidationException {

        UserController controller = new UserController();
        User user = new User("name@mail.ru", "Zzerrgg", LocalDate.of(1990, 3, 30));
        user.setName("Дмитрий");
        controller.addUser(user);
        User user2 = new User("name@mail.ru", "Bberrgen", LocalDate.of(2005, 6, 25));
        user2.setId(100);
        assertThrows(ValidationException.class, () -> controller.updateUser(user2));
    }

}