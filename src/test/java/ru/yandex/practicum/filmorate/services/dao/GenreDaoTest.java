package ru.yandex.practicum.filmorate.services.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreDaoTest {
    private final GenreService genreService;

    @Test
    public void testGetAll() {
        List<Genre> genreList = genreService.getAll();

        check(genreList.get(0));
        check(genreList.get(1));
        check(genreList.get(2));
        check(genreList.get(3));
        check(genreList.get(4));
        check(genreList.get(5));
    }

    @Test
    public void testGet() {
        check(genreService.get(1));
    }

    private void check(Genre genre) {
        int id = genre.getId();
        String name = genre.getName();
        String expectedName;

        switch (id) {
            case 1:
                expectedName = "Комедия";
                break;
            case 2:
                expectedName = "Драма";
                break;
            case 3:
                expectedName = "Мультфильм";
                break;
            case 4:
                expectedName = "Триллер";
                break;
            case 5:
                expectedName = "Документальный";
                break;
            case 6:
                expectedName = "Боевик";
                break;
            default:
                expectedName = "";
        }

        assertThat(name, is(expectedName));
    }
}