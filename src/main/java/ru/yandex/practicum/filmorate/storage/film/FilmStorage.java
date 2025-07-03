package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    public Collection<Film> getFilms();

    public Optional<Film> addFilm(Film film);

    public Optional<Film> updateFilm(Film film);

    boolean ifFilmExists(Integer filmId);

    void addLike(Integer userId, Integer filmId);

    void deleteLike(Integer userId, Integer filmId);

    Collection<Film> getRatedFilms(Integer count);

    Optional<Film> getFilmById(Integer id);
}
