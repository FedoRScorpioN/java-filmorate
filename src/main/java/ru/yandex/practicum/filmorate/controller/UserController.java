package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validationUsers(user);
        checkUsers(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создание пользователя {} прошло успешно", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validationUsers(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя не существует. Невозможно обновить данные");
        }
        checkUsers(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя {} успешно обновлены", user.getName());
        return user;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private void validationUsers(@Valid @RequestBody User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин пользователя: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Указанная Дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private void checkUsers(@RequestBody User userToAdd) {
        boolean exists = users.values().stream()
                .anyMatch(user -> alreadyExistsUsers(userToAdd, user));
        if (exists) {
            log.warn("E-mail пользователя: {}", userToAdd);
            throw new ValidationException("Пользователь с таким E-mail или Логином уже существует");
        }
    }

    private boolean alreadyExistsUsers(User userToAdd, User user) {
        return userToAdd.getLogin().equals(user.getLogin()) ||
                userToAdd.getEmail().equals(user.getEmail());
    }
}