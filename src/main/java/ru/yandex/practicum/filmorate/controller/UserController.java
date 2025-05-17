package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("пользователь с id {} добавлен", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
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
        return oldUser;


    }

    private int getNextId() {
        int currentId = users.keySet()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
        return ++currentId;
    }
}
