package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmWithGenresExtractor;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDBStorage.class, UserRowMapper.class, FilmWithGenresExtractor.class,
        MpaRowMapper.class, GenreRowMapper.class, FilmDBStorage.class, MpaStorage.class, GenreDBStorage.class})
class FilmorateApplicationTests {
    @Autowired
    private final UserDBStorage userStorage;
    @Autowired
    private final FilmDBStorage filmStorage;
    @Autowired
    private final GenreDBStorage genreStorage;
    @Autowired
    private final MpaStorage mpaStorage;
    private User user;
    private User user2;
    private Film film1;
    private Film film2;
    @Autowired
    private JdbcTemplate jdbc;


    @BeforeEach
    void cleanDb() {
        jdbc.execute("DELETE FROM Film_Genre");
        jdbc.execute("DELETE FROM Films");
        jdbc.execute("DELETE FROM FriendShip");
        jdbc.execute("DELETE FROM Users");
        jdbc.execute("ALTER TABLE Films ALTER COLUMN film_id RESTART WITH 1");
        jdbc.execute("ALTER TABLE Users ALTER COLUMN user_id RESTART WITH 1");
    }

    @BeforeEach
    public void init() {
        user = User.builder()
                .name("Anna")
                .email("aaa@aaa.ru")
                .login("aa")
                .birthday(LocalDate.of(2000, 12, 1))
                .build();

        user2 = User.builder()
                .name("Oleg")
                .email("ooo@ooo.ru")
                .login("oo")
                .birthday(LocalDate.of(2001, 1, 2))
                .build();

        film1 = Film.builder()
                .name("Film")
                .description("Filmdesc")
                .duration(30)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        film2 = Film.builder()
                .name("Film2")
                .description("Filmdesc2")
                .duration(300)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
    }


    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.addUser(user);
        Optional<User> userOptional1 = userStorage.findUserById(1);

        assertThat(userOptional1)
                .isPresent()
                .get()
                .extracting("id", "name", "email", "login", "birthday")
                .containsExactly(1, "Anna", "aaa@aaa.ru", "aa", LocalDate.of(2000, 12, 1));

    }

    @Test
    public void testGetUsers() {
        Optional<User> ouser1 = userStorage.addUser(user);
        Optional<User> ouser2 = userStorage.addUser(user2);
        List<User> users = userStorage.getUsers().stream().toList();
        assertEquals(2, users.size());
    }

    @Test
    public void testUpdateUser() {
        Optional<User> ouser1 = userStorage.addUser(user);
        user.setLogin("qq");
        user.setName("Yulia");
        user.setEmail("Yulia@qq.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Optional<User> updated = userStorage.updateUser(user);
        assertThat(updated)
                .isPresent()
                .get()
                .extracting("id", "name", "email", "login", "birthday")
                .containsExactly(1, "Yulia", "Yulia@qq.ru", "qq", LocalDate.of(1990, 1, 1));

    }

    @Test
    public void testIfUserExists() {
        Optional<User> ouser = userStorage.addUser(user);
        assertTrue(userStorage.ifUserExists(1));
        assertFalse(userStorage.ifUserExists(2));
    }

    @Test
    public void testAddFriendship() {
        Optional<User> ouser = userStorage.addUser(user);
        Optional<User> ouser2 = userStorage.addUser(user2);
        userStorage.addFriendShip(1, 2);
        assertEquals(1, userStorage.getFriends(1).size());
        assertEquals(0, userStorage.getFriends(2).size());
        assertEquals(2, userStorage.getFriends(1).stream().findFirst().get().getId());
    }

    @Test
    public void testDeleteFriendShip() {
        Optional<User> ouser = userStorage.addUser(user);
        Optional<User> ouser2 = userStorage.addUser(user2);
        userStorage.addFriendShip(1, 2);
        userStorage.addFriendShip(2, 1);
        userStorage.deleteFriendShip(1, 2);
        userStorage.deleteFriendShip(2, 1);
        assertEquals(0, userStorage.getFriends(1).size());
        assertEquals(0, userStorage.getFriends(2).size());

    }

    @Test
    public void testGetMutualFriends() {
        User mfriend = User.builder()
                .name("Friend")
                .email("f@f.ru")
                .login("f")
                .birthday(LocalDate.of(2001, 1, 2)).build();
        Optional<User> ouser1 = userStorage.addUser(user);
        Optional<User> ouser2 = userStorage.addUser(user2);
        Optional<User> ouser3 = userStorage.addUser(mfriend);
        userStorage.addFriendShip(user.getId(), mfriend.getId());
        userStorage.addFriendShip(user2.getId(), mfriend.getId());
        List<User> mutual = userStorage.getMutualFriends(user.getId(), user2.getId()).stream().toList();
        assertEquals(1, mutual.size());
        assertThat(mutual.getFirst())
                .extracting("id", "name", "email", "login", "birthday")
                .containsExactly(3, "Friend", "f@f.ru", "f", LocalDate.of(2001, 1, 2));


    }

    @Test
    public void testGetFilmById() {
        film1.getGenres().add(genreStorage.getDBGenresById(1));
        film1.setMpa(mpaStorage.getMpa(1));
        Optional<Film> optionalFilm = filmStorage.addFilm(film1);
        Optional<Film> getFilm = filmStorage.getFilmById(1);
        assertThat(getFilm)
                .isPresent()
                .get()
                .extracting("id", "description", "name", "duration", "releaseDate", "mpa")
                .containsExactly(1, "Filmdesc", "Film", 30, LocalDate.of(2000, 1, 1),
                        mpaStorage.getMpa(1));

    }

    @Test
    public void testGetFilms() {
        film1.getGenres().add(genreStorage.getDBGenresById(1));
        film1.setMpa(mpaStorage.getMpa(1));
        film2.getGenres().add(genreStorage.getDBGenresById(1));
        film2.setMpa(mpaStorage.getMpa(1));
        Optional<Film> optionalFilm = filmStorage.addFilm(film1);
        Optional<Film> optionalFilm2 = filmStorage.addFilm(film2);
        List<Film> films = filmStorage.getFilms().stream().toList();
        assertEquals(2, films.size());
    }

    @Test
    void testUpdateFilm() {
        film1.getGenres().add(genreStorage.getDBGenresById(1));
        film1.setMpa(mpaStorage.getMpa(1));
        Optional<Film> optionalFilm = filmStorage.addFilm(film1);
        film2.setId(1);
        film2.setMpa(mpaStorage.getMpa(2));
        film1.getGenres().add(genreStorage.getDBGenresById(2));
        Optional<Film> optionalFilm2 = filmStorage.updateFilm(film2);
        Optional<Film> getFilm = filmStorage.getFilmById(1);
        assertThat(getFilm)
                .isPresent()
                .get()
                .extracting("id", "description", "name", "duration", "releaseDate", "mpa")
                .containsExactly(1, "Filmdesc2", "Film2", 300, LocalDate.of(2000, 1, 1),
                        mpaStorage.getMpa(2));

    }

    @Test
    public void testIfFilmExists() {
        film1.getGenres().add(genreStorage.getDBGenresById(1));
        film1.setMpa(mpaStorage.getMpa(1));
        Optional<Film> optionalFilm = filmStorage.addFilm(film1);
        assertTrue(filmStorage.ifFilmExists(1));
        assertFalse(filmStorage.ifFilmExists(2));
    }

}