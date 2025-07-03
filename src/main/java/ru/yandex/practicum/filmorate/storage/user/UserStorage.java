package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    public Collection<User> getUsers();

    public Optional<User> addUser(User user);

    public Optional<User> updateUser(User user);

    boolean ifUserExists(Integer userId);

    void addFriendShip(Integer userId, Integer friendId);

    void deleteFriendShip(Integer userId, Integer friendId);

    Collection<User> getFriends(Integer id);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);
}
