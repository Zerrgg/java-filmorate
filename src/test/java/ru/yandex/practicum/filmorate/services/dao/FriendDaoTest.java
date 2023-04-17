package ru.yandex.practicum.filmorate.services.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FriendService;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FriendDaoTest {
    private final FriendService friendService;
    private final UserService userService;
    private final User user1 = new User(1, "Дима", "Zerg", "Zerg@mail.ru", LocalDate.of(1984, 3, 26));
    private final User user2 = new User(2, "Митя", "JavaMan", "JavaMan@mail.ru", LocalDate.of(1980, 1, 1));
    private final User user3 = new User(3, "Митяй", "Gudzon", "Gudzon@mail.ru", LocalDate.of(1990, 10, 10));

    @Test
    void testAddUsers() {
        User result1 = userService.add(user1);
        User result2 = userService.add(user2);
        User result3 = userService.add(user3);

        checkUsers(result1, user1);
        checkUsers(result2, user2);
        checkUsers(result3, user3);
    }

    @Test
    void testAddFriends() {
        testAddUsers();
        friendService.add(1, 3);
        friendService.add(2, 3);
        friendService.add(3, 1);
        friendService.add(3, 2);
    }

    @Test
    void testGetFriends() {
        testAddFriends();
        List<User> result = friendService.get(1);
        List<User> result2 = friendService.get(2);

        checkUsers(result.get(0), user3);
        checkUsers(result2.get(0), user3);
    }

    @Test
    void testGetMutualUsersFriends() {
        testAddFriends();
        List<User> result = friendService.getMutualUsersFriends(1, 2);

        checkUsers(result.get(0), user3);
    }

    @Test
    void testDeleteFriend() {
        testAddFriends();
        friendService.delete(1, 3);
        List<User> result = friendService.get(1);

        assertThat(result.isEmpty(), is(true));
    }

    private void checkUsers(User result, User expected) {
        assertThat(result.getId(), is(expected.getId()));
        assertThat(result.getEmail(), is(expected.getEmail()));
        assertThat(result.getLogin(), is(expected.getLogin()));
        assertThat(result.getName(), is(expected.getName()));
        assertThat(result.getBirthday(), is(expected.getBirthday()));
    }
}
