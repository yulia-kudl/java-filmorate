package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public Collection<User> getUsers();

    public User addUser(User user);

    public User updateUser(User user);

    boolean ifUserExists(Integer userId);

    void addFriendShip(Integer userId, Integer friendId);

    void deleteFriendShip(Integer userId, Integer friendId);

    Collection<User> getFriends(Integer id);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);
}
