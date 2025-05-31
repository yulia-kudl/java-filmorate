package ru.yandex.practicum.filmorate.controller;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//здесь нет тестов на валидацию полей, тк они проверяются через аннотации

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void addFilmOk() {
        Film film = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();

        Film film2 = filmController.addFilm(film);
        assertEquals(1, film2.getId(), "incorrect id");
        assertEquals("Inception", film2.getName(), "incorrect name");
        assertEquals(LocalDate.of(2010, 7, 16), film2.getReleaseDate(), "incorrect date");
        assertEquals(148, film2.getDuration(), "incorrect duration");
        assertEquals(1, filmController.findAll().size(), "incorrect films size");
        assertEquals("Inception", filmController.findAll().stream().toList().getFirst().getName(), "incorrect name");
    }



    @Test
    void updateFilm() {
        Film film = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();

        Film film2 = filmController.addFilm(film);
        Film film3 = Film.builder()
                .name("Inception2")
                .description("A film about dreams. 2")
                .releaseDate(LocalDate.of(2012, 7, 16))
                .duration(149)
                .id(1)
                .build();

        Film film4 = filmController.updateFilm(film3);

        assertEquals(1, film4.getId(), "incorrect id");
        assertEquals("Inception2", film4.getName(), "incorrect name");
        assertEquals(LocalDate.of(2012,7,16), film4.getReleaseDate(), "incorrect date");
        assertEquals(149, film4.getDuration(), "incorrect duration");
        assertEquals(1, filmController.findAll().size(), "incorrect films size");
        assertEquals("Inception2", filmController.findAll().stream().toList().getFirst().getName(), "incorrect name");

    }


    @Test
    void updateFilmIncorrectId() {
        Film film = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2000, 7, 16))
                .duration(148)
                .build();
        Film film2 = filmController.addFilm(film);
        Film film3  = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2000, 7, 16))
                .duration(148)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film3);
        });
    }

    void updateFilmNoId() {
        Film film = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2000, 7, 16))
                .duration(148)
                .build();
        Film film2 = filmController.addFilm(film);
        Film film3  = Film.builder()
                .name("Inception")
                .description("A film about dreams.")
                .releaseDate(LocalDate.of(2000, 7, 16))
                .duration(148)
                .id(10)
                .build();
        assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film3);
        });
    }
}