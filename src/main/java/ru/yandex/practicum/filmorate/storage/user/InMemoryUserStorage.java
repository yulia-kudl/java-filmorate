package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@Deprecated
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> addUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        user.setFriends(null);
        users.put(user.getId(), user);
        log.info("пользователь с id {} добавлен", user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (user.getId() == null) {
            log.info("id должен быть указан");
            throw new ValidationException("id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            log.info("пользователя с id {} нет", user.getId());
            throw new ValidationException("пользователя с таким id нет");
        }

        User oldUser = users.get(user.getId());

        if (user.getEmail() != null && user.getEmail().contains("@")) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getLogin() != null && !user.getLogin().contains(" ")) {
            oldUser.setLogin(user.getLogin());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        if (user.getBirthday().isBefore(LocalDate.now())) {
            oldUser.setBirthday(user.getBirthday());
        }
        log.info("данные по пользователю {} обновлены", user.getId());
        return Optional.of(oldUser);

    }

    @Override
    public boolean ifUserExists(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public void addFriendShip(Integer userId, Integer friendId) {
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriendShip(Integer userId, Integer friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return users.values().stream()
                .filter(user -> users.get(id).getFriends().contains(user.getId()))
                .toList();
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        return users.get(userId).getFriends()
                .stream()
                .filter(id -> users.get(otherId).getFriends().contains(id))
                .map(id -> users.get(id))
                .toList();

    }

    private int getNextId() {
        int currentId = users.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
        return ++currentId;
    }

}
