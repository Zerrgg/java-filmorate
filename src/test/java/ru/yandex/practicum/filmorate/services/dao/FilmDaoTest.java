package ru.yandex.practicum.filmorate.services.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDaoTest {

    private final FilmService filmService;

    private final Genre genre = new Genre(1,"Комедия");
    private final Mpa mpa = new Mpa(1, "G");

    private final Film film1 = new Film(1, "Java", "Фильм про программистов",
            120, LocalDate.now(), mpa, List.of(genre));
    private final Film film2 = new Film(2, "Java2", "Фильм про программистов2",
            120, LocalDate.now(), mpa, List.of(genre));
    private final Film film3 = new Film(3, "Java3", "Фильм про программистов3",
            120, LocalDate.now(), mpa, List.of(genre));
    private final User user = new User(1, "Дима", "Zerg", "Zerg@mail.ru", LocalDate.of(1984, 3, 26));

    @Test
    void testAdd() {
        Film result1 = filmService.add(film1);
        Film result2 = filmService.add(film2);
        Film result3 = filmService.add(film3);

        check(result1, film1);
        check(result2, film2);
        check(result3, film3);
    }

    @Test
    void testUpdate() {
        testAdd();

        Film updatedFilm = film1;
        updatedFilm.setName("НовоеИмя");
        Film result = filmService.update(updatedFilm);

        check(result, updatedFilm);
    }

    @Test
    void testGet() {
        testAdd();

        check(filmService.get(1), film1);
        check(filmService.get(2), film2);
        check(filmService.get(3), film3);
    }

    @Test
    void testGetAll() {
        testAdd();

        List<Film> result = filmService.getAll();

        assertThat(result.size(), is(3));

        check(result.get(0), film1);
        check(result.get(1), film2);
    }

    @Test
    void testGetFilmsList() {
        List<Film> expectedList = new ArrayList<>();
        expectedList.add(film1);
        expectedList.add(film2);

        filmService.add(film1);
        filmService.add(film2);

        List<Film> result = filmService.getAll();

        assertThat(result.get(0), is(expectedList.get(0)));
        assertThat(result.get(1), is(expectedList.get(1)));
    }

    private void check(Film result, Film expected) {
        assertThat(result.getId(), is(expected.getId()));
        assertThat(result.getName(), is(expected.getName()));
        assertThat(result.getDescription(), is(expected.getDescription()));
        assertThat(result.getDuration(), is(expected.getDuration()));
        assertThat(result.getReleaseDate(), is(expected.getReleaseDate()));
        assertThat(result.getMpa(), is(expected.getMpa()));
        assertThat(result.getGenres(), is(expected.getGenres()));
    }
}
