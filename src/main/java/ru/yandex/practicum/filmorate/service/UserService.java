package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (!userStorage.ifUserExists(user.getId())) {
            throw new NotFoundException("пользователя с ID " + user.getId() + " не найдено");
        }
        return userStorage.updateUser(user);
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!userStorage.ifUserExists(userId)) {
            throw new NotFoundException("пользователь с ID " + userId + " не найдено");
        }
        if (!userStorage.ifUserExists(friendId)) {
            throw new NotFoundException("пользователя с ID " + friendId + " не найдено");
        }
        userStorage.addFriendShip(userId, friendId);

    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.ifUserExists(userId)) {
            throw new NotFoundException("пользователь с ID " + userId + " не найдено");
        }
        if (!userStorage.ifUserExists(friendId)) {
            throw new NotFoundException("пользователя с ID " + friendId + " не найдено");
        }
        userStorage.deleteFriendShip(userId, friendId);
    }

    public Collection<User> getFriends(Integer id) {
        if (!userStorage.ifUserExists(id)) {
            throw new NotFoundException("пользователя с ID " + id + " не найдено");
        }
        return userStorage.getFriends(id);
    }

    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        if (!userStorage.ifUserExists(userId)) {
            throw new NotFoundException("пользователь с ID " + userId + " не найдено");
        }
        if (!userStorage.ifUserExists(otherId)) {
            throw new NotFoundException("пользователя с ID " + otherId + " не найдено");
        }

        return userStorage.getMutualFriends(userId, otherId);
    }
}
