package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

      /*  if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("дата {} до 1895-12-28", film.getReleaseDate());
            throw new ValidationException("дата релиза некорректная");
        } */
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("новый фильм с ид {} добавлен", film.getId());
        return  film;
    }

    @PutMapping Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            log.info("id не может быть пустым");
            throw new ValidationException("id должен быть указан");
        }
        if (!films.containsKey(film.getId())) {
            log.info("фильма с ид {} нет", film.getId());
            throw new ValidationException("фильма с таким id нет");
        }

        Film oldFilm = films.get(film.getId());

        if (film.getName() != null && !film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }
        if (film.getDescription().length() <= 200) {
            oldFilm.setDescription(film.getDescription());
        }

        if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() > 0) {
            oldFilm.setDuration(film.getDuration());
        }
        log.info("фильм с id {} обновлен", film.getId());
        return oldFilm;
    }

    private int getNextId() {
        int currentId = films.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
        return ++currentId;
    }
}
