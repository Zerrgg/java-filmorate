package ru.yandex.practicum.filmorate.services.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoTest {
    private final UserService userService;

    private final User user1 = new User(1, "Дима", "Zerg", "Zerg@mail.ru", LocalDate.of(1984, 3, 26));
    private final User user2 = new User(2, "Митя", "JavaMan", "JavaMan@mail.ru", LocalDate.of(1980, 1, 1));
    private final User user3 = new User(3, "Митяй", "Gudzon", "Gudzon@mail.ru", LocalDate.of(1990, 10, 10));

    @Test
    void testAdd() {
        User result1 = userService.add(user1);
        User result2 = userService.add(user2);
        User result3 = userService.add(user3);

        check(result1, user1);
        check(result2, user2);
        check(result3, user3);
    }

    @Test
    void testUpdate() {
        testAdd();
        User updatedUser = user2;
        updatedUser.setName("НовоеИмя");

        User result = userService.update(updatedUser);

        check(result, updatedUser);
    }

    @Test
    void testGet() {
        testAdd();
        User result1 = userService.get(1);
        User result2 = userService.get(2);
        User result3 = userService.get(3);

        check(result1, user1);
        check(result2, user2);
        check(result3, user3);
    }

    private void check(User result, User expected) {
        assertThat(result.getId(), is(expected.getId()));
        assertThat(result.getEmail(), is(expected.getEmail()));
        assertThat(result.getLogin(), is(expected.getLogin()));
        assertThat(result.getName(), is(expected.getName()));
        assertThat(result.getBirthday(), is(expected.getBirthday()));
    }
}
