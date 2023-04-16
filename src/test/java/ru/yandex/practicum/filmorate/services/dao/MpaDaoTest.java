package ru.yandex.practicum.filmorate.services.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MpaDaoTest {
    private final MpaService mpaService;

    @Test
    public void testGetAll() {
        List<Mpa> ratingList = mpaService.getAll();

        check(ratingList.get(0));
        check(ratingList.get(1));
        check(ratingList.get(2));
        check(ratingList.get(3));
        check(ratingList.get(4));
    }

    @Test
    public void testGet() {
        check(mpaService.get(1));
    }

    private void check(Mpa mpa) {
        int id = mpa.getId();
        String name = mpa.getName();
        String expectedName;

        switch (id) {
            case 1:
                expectedName = "G";
                break;
            case 2:
                expectedName = "PG";
                break;
            case 3:
                expectedName = "PG-13";
                break;
            case 4:
                expectedName = "R";
                break;
            case 5:
                expectedName = "NC-17";
                break;
            default:
                expectedName = "";
        }

        assertThat(name, is(expectedName));
    }
}