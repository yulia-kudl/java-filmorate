package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Primary
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    static final String GET_USERS = "SELECT * FROM Users ";
    static final String GET_USER_BY_ID = "SELECT * FROM Users WHERE user_id = ? ";
    static final String ADD_USER = "INSERT INTO Users ( email, login, name, birthday) " +
            " VALUES (?, ?, ?, ?);";
    static final String UPDATE_USER = "UPDATE Users SET email = ? , login = ?, name = ?, birthday = ? WHERE user_id = ?;";
    static final String CHECK_IS_USER_EXISTS = "SELECT COUNT(*) FROM Users WHERE user_id = ?;";
    static final String ADD_FRIENDSHIP = "INSERT INTO FriendShip (request_user, to_user) VALUES (?, ?);";
    static final String DELETE_FRIENDSHIP = "DELETE FROM FriendShip WHERE request_user = ? AND to_user = ?;";
    static final String GET_FRIENDS = "SELECT * FROM Users u JOIN FriendShip f ON u.user_id = f.to_user WHERE f.request_user =? ";
    static final String GET_MUTUAL_FRIENDS = "SELECT * " +
            "FROM (" + GET_FRIENDS + ") AS F1 " +
            "JOIN (" + GET_FRIENDS + ") AS F2 ON F1.user_id = F2.user_id";

    @Override
    public Collection<User> getUsers() {
        return jdbc.query(GET_USERS, mapper);
    }

    @Override
    public Optional<User> addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return findUserById(user.getId());
    }

    @Override
    public Optional<User> updateUser(User user) {
        jdbc.update(UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return findUserById(user.getId());
    }

    @Override
    public boolean ifUserExists(Integer userId) {
        Integer count = jdbc.queryForObject(CHECK_IS_USER_EXISTS, Integer.class, userId);
        return count != 0;
    }

    @Override
    public void addFriendShip(Integer userId, Integer friendId) {
        jdbc.update(ADD_FRIENDSHIP, userId, friendId);

    }

    @Override
    public void deleteFriendShip(Integer userId, Integer friendId) {
        jdbc.update(DELETE_FRIENDSHIP, userId, friendId);
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return jdbc.query(GET_FRIENDS, mapper, id);
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        return jdbc.query(GET_MUTUAL_FRIENDS, mapper, userId, otherId);
    }

    public Optional<User> findUserById(int id) {
        List<User> users = jdbc.query(GET_USER_BY_ID, mapper, id);
        return users.stream().findFirst();
    }
}
