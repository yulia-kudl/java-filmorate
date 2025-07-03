package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmWithGenresExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        Map<Integer, Film> filmMap = new LinkedHashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            Film film = filmMap.get(filmId);

            if (film == null) {
                film = new Film();
                film.setId(rs.getInt("film_id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setGenres(new HashSet<>());
                Mpa rate =   Mpa.builder()
                        .id(rs.getInt("rate_id"))
                        .name(rs.getString("rate_value"))
                        .build();
                film.setMpa(rate);
                // MPA rating

                film.setGenres(new HashSet<>());
                filmMap.put(filmId, film);
            }

            int genreId = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rs.getString("genre_name"));
                film.getGenres().add(genre);
            }
        }

        return new ArrayList<>(filmMap.values());
    }

}
