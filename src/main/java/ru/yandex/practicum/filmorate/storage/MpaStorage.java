package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class MpaStorage {

    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;
    static final String CHECK_MPA = "SELECT COUNT(*) FROM Rate WHERE rate_id = ?;";
    static final String GET_ALL_MPA = "SELECT * FROM Rate ;";
    static final String GET_MPA_BY_ID = "SELECT * FROM Rate WHERE rate_id = ?;";


    public boolean ifMPAExists(int mpa) {
        Integer count = jdbc.queryForObject(CHECK_MPA, Integer.class, mpa);
        return count != 0;
    }

    public Collection<Mpa> getAllMpa() {
        return jdbc.query(GET_ALL_MPA, mapper);
    }

    public Mpa getMpa(Integer id) {
        return jdbc.queryForObject(GET_MPA_BY_ID, mapper, id);
    }
}
