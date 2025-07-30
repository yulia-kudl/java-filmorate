package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreDBStorage genreDBStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Optional<Film> addFilm(Film film) {
        if (film.getMpa() != null && !mpaStorage.ifMPAExists(film.getMpa().getId())) {
            throw new NotFoundException("mpa c ID" + film.getMpa().getId() + "не найдено");
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!genreDBStorage.ifGenreExists(genre.getId()))
                    throw new NotFoundException("genre c ID" + genre.getId() + "не найдено");
            }
        }
        return filmStorage.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
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

    public Optional<Film> getFilmById(Integer id) {
        if (!filmStorage.ifFilmExists(id)) {
            throw new NotFoundException("фильма с ID " + id + "не существует");
        }
        return filmStorage.getFilmById(id);
    }
}
