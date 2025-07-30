package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class GenreDBStorage {

    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;
    static final String CHECK_GENRE = "SELECT COUNT(*) FROM Genre WHERE genre_id = ?;";
    static final String GET_GENRES = "SELECT * FROM Genre;";
    static final String GET_GENRES_BY_ID = "SELECT * FROM Genre WHERE genre_id =?;";

    public boolean ifGenreExists(Integer id) {
        Integer count = jdbc.queryForObject(CHECK_GENRE, Integer.class, id);
        return count != 0;
    }

    public Collection<Genre> getDBGenres() {
        return jdbc.query(GET_GENRES, mapper);
    }

    public Genre getDBGenresById(Integer id) {
        return jdbc.queryForObject(GET_GENRES_BY_ID, mapper, id);
    }
}
