package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.ifFilmExists(film.getId())) {
            throw new NotFoundException("фильма с ID " + film.getId() + " не найдено");
        }
        return filmStorage.updateFilm(film);
    }


    public void addLike(Integer userId, Integer filmId) {
        if (!filmStorage.ifFilmExists(filmId)) {
            throw new NotFoundException("фильма с ID " + filmId + " не найдено");
        }
        if (!userStorage.ifUserExists(userId)) {
            throw new NotFoundException("пользователя с ID " + userId + " не найдено");
        }
        filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(Integer userId, Integer filmId) {
        if (!filmStorage.ifFilmExists(filmId)) {
            throw new NotFoundException("фильма с ID " + filmId + " не найдено");
        }
        if (!userStorage.ifUserExists(userId)) {
            throw new NotFoundException("пользователя с ID " + userId + " не найдено");
        }
        filmStorage.deleteLike(userId, filmId);

    }

    public Collection<Film> getMostLikedFilms(Integer count) {

        return filmStorage.getRatedFilms(count);
    }
}
