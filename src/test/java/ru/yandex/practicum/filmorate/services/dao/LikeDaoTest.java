//package ru.yandex.practicum.filmorate.services.dao;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.Genre;
//import ru.yandex.practicum.filmorate.model.Mpa;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.services.FilmService;
//import ru.yandex.practicum.filmorate.services.LikeService;
//import ru.yandex.practicum.filmorate.services.UserService;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.CoreMatchers.is;
//
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//class LikeDaoTest {
//
//    private final FilmService filmService;
//    private final UserService userService;
//    private final LikeService likeService;
//    private final Genre genre = new Genre(1, "Комедия");
//    private final Mpa mpa = new Mpa(1, "G");
//    private final Film film1 = new Film(1, "Java", "Фильм про программистов",
//             120, LocalDate.now(), mpa, List.of(genre));
//    private final Film film2 = new Film(2, "Java2", "Фильм про программистов2",
//             120, LocalDate.now(), mpa, List.of(genre));
//    private final Film film3 = new Film(3, "Java3", "Фильм про программистов3",
//             120, LocalDate.now(), mpa, List.of(genre));
//    private final User user1 = new User(1, "Дима", "Zerg", "Zerg@mail.ru", LocalDate.of(1984, 3, 26));
//    private final User user2 = new User(2, "Митя", "JavaMan", "JavaMan@mail.ru", LocalDate.of(1980, 1, 1));
//
//    @Test
//    void testAddLikeFilm() {
//        filmService.add(film1);
//        filmService.add(film2);
//        filmService.add(film3);
//
//        userService.add(user1);
//        userService.add(user2);
//
//        likeService.add(film3.getId(), user1.getId());
//        likeService.add(film2.getId(), user1.getId());
//        likeService.add(film3.getId(), user2.getId());
//
//    }
//
//    @Test
//    void testDeleteLikeFilm() {
//        testAddLikeFilm();
//
//        likeService.delete(film3.getId(), user1.getId());
//        likeService.delete(film3.getId(), user2.getId());
//
//        List<Film> result = likeService.getPopularFilms(2);
//
//        assertThat(result.get(0), is(film2));
//        assertThat(result.get(1), is(film3));
//    }
//
//    @Test
//    void testGetLikedFilmsWithLimit() {
//        testAddLikeFilm();
//
//        List<Film> result = likeService.getPopularFilms(2);
//
//        assertThat(result.get(0), is(film3));
//        assertThat(result.get(1), is(film2));
//    }
//}
