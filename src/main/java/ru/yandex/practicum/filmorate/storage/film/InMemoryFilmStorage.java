package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        film.setLikes(null);
        films.put(film.getId(), film);
        log.info("новый фильм с ид {} добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
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

    @Override
    public boolean ifFilmExists(Integer filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getRatedFilms(Integer count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }


    private int getNextId() {
        int currentId = films.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
        return ++currentId;
    }
}
