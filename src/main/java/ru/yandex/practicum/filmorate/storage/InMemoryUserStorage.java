package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        validationUsers(user);
        checkUsers(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создание пользователя {} прошло успешно", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validationUsers(user);
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Такого пользователя не существует. Невозможно обновить данные");
        }
        users.put(user.getId(), user);
        log.info("Данные пользователя {} успешно обновлены", user.getName());
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private void validationUsers(User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин пользователя: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getEmail().isBlank() || user.getEmail().contains(" ") || !user.getEmail().contains("@")) {
            log.warn("Почта: {}, не указана, содержит пробелы или не содержит символ @.", user.getEmail());
            throw new ValidationException("Почта не может быть пустой и содержать пробелы, а символ @ обязателен.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Указанная Дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Не указано Имя пользователя. В качестве Имени пользователя использован Логин: {}", user.getLogin());
        }
    }

    private void checkUsers(User userToAdd) {
        boolean exists = users.values().stream()
                .anyMatch(user -> alreadyExistsUsers(userToAdd, user));
        if (exists) {
            log.warn("E-mail пользователя: {} уже существует.", userToAdd);
            throw new ValidationException("Пользователь с таким E-mail или Логином уже существует");
        }
    }

    private boolean alreadyExistsUsers(User userToAdd, User user) {
        return userToAdd.getLogin().equals(user.getLogin()) ||
                userToAdd.getEmail().equals(user.getEmail());
    }

    @Override
    public User getByIdUser(int id) {
        return users.get(id);
    }

    @Override
    public User deleteByIdUser(int id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }
}