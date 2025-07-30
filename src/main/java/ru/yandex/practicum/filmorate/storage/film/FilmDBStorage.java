package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmWithGenresExtractor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
@Primary
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbc;
    private final FilmWithGenresExtractor extractor;


    static final String INSERT_FILM = "INSERT INTO Films ( name, description, release_date, duration, rate_id)" +
            "VALUES (?, ?, ?, ?, ?);";
    static final String SELECT_ALL_FILMS = "SELECT f.*, r.rate AS rate_value, r.rate_id AS rate_id, r.rate AS rate_value, fg.genre_id AS genre_id, g.name AS genre_name FROM Films f LEFT JOIN rate r ON f.rate_id = r.rate_id LEFT JOIN film_genre fg ON fg.film_id = f.film_id LEFT JOIN genre g ON g.genre_id = fg.genre_id;";
    static final String SELECT_FILM_BY_ID = "SELECT f.*, r.rate AS rate_value, r.rate_id AS rate_id, r.rate AS rate_value, fg.genre_id AS genre_id, g.name AS genre_name FROM Films f LEFT JOIN rate r ON f.rate_id = r.rate_id LEFT JOIN film_genre fg ON fg.film_id = f.film_id  LEFT JOIN genre g ON g.genre_id = fg.genre_id WHERE f.film_id =? ;";
    static final String UPDATE_FILM = "UPDATE films SET name = ? , description = ?, release_date = ?, duration = ?, rate_id = ? WHERE film_id = ?;";
    static final String CHECK_IF_EXISTS = "SELECT COUNT(*) FROM Films WHERE film_id = ?";
    static final String ADD_LIKE = "INSERT INTO Likes VALUES ( ?, ?)";
    static final String DELETE_LIKE = "DELETE FROM Likes WHERE film_id = ? AND user_id = ?;";
    static final String ADD_FILM_GENRE = "INSERT INTO Film_genre VALUES (?,?);";
    static final String GET_RATED_FILMS_ID = "SELECT film_id FROM Likes GROUP BY film_id ORDER BY COUNT(user_id) desc LIMIT ? ";
    static final String GET_RATED_FILMS = "SELECT f.*,  r.rate AS rate_value, r.rate_id AS rate_id , fg.genre_id AS genre_id, l.likes, " +
            "g.name AS genre_name  " +
            "FROM films f " +
            "LEFT JOIN (" +
            "    SELECT film_id, COUNT(user_id) AS likes " +
            "    FROM Likes" +
            "    GROUP BY film_id " +
            " LIMIT ?" +
            ") l ON f.film_id = l.film_id LEFT JOIN rate r on f.rate_id = r.rate_id " +
            "LEFT JOIN film_genre fg on f.film_id = fg.film_id " +
            "LEFT JOIN genre g on fg.genre_id = g.genre_id " +
            "ORDER BY likes DESC ;";

    @Override
    public Collection<Film> getFilms() {
        return jdbc.query(SELECT_ALL_FILMS, extractor);

    }


    @Override
    public Optional<Film> addFilm(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                ps.setInt(5, film.getMpa().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        addFilmGenres(film.getId(), film.getGenres());
        return getFilmById(film.getId());

    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        jdbc.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        addFilmGenres(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }

    @Override
    public boolean ifFilmExists(Integer filmId) {
        Integer count = jdbc.queryForObject(CHECK_IF_EXISTS, Integer.class, filmId);
        return count != 0;
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        jdbc.update(ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        jdbc.update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public Collection<Film> getRatedFilms(Integer count) {

        return jdbc.query(GET_RATED_FILMS, extractor, count);
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        List<Film> films = jdbc.query(SELECT_FILM_BY_ID, extractor, id);
        assert films != null;
        return films.stream().findFirst();
    }

    private void addFilmGenres(int filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbc.update(ADD_FILM_GENRE, filmId, genre.getId());
        }
    }
}
