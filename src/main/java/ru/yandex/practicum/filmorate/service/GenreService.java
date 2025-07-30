package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDBStorage;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDBStorage genreDBStorage;

    public Collection<Genre> getGenres() {
        return genreDBStorage.getDBGenres();
    }

    public Genre getGenreById(Integer id) {
        if (!genreDBStorage.ifGenreExists(id)) {
            throw new NotFoundException("жанра с ID " + id + " не найдено");
        }
        return genreDBStorage.getDBGenresById(id);
    }
}
