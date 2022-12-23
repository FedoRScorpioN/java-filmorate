package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private int counter = 1;
    private final Validator validator;
    private final UserStorage userStorage;

    @Autowired
    public UserService(Validator validator, UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        log.info("Сформирован список пользователей.");
        return userStorage.findAllUsers();
    }

    private User getUserStored(final String expectedId) {
        final int userId = parseId(expectedId);
        if (userId == Integer.MIN_VALUE) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден.", expectedId));
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id -  '%d' отсутствует.", userId));
        }
        return user;
    }

    private Integer parseId(final String id) {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public User deleteByIdUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Невозможно удалить пользователя, так как он не существует.");
        }
        log.info("Пользователь под номером: '{}' удален", id);
        return userStorage.deleteByIdUser(id);
    }

    public void addFriends(final String firstId, final String secondId) {
        User user = getUserStored(firstId);
        User friend = getUserStored(secondId);
        userStorage.addFriend(user.getId(), friend.getId());
        log.info("Пользователи: '{}' и '{}' теперь Друзья.", firstId, secondId);
    }

    public void removeFriends(final String firstId, final String secondId) {
        User user = getUserStored(firstId);
        User friend = getUserStored(secondId);
        userStorage.deleteFriend(user.getId(), friend.getId());
        log.info("Пользователи: '{}' и '{}' перестали дружить.", firstId, secondId);
    }

    public Collection<User> getFriendsListById(String firstId) {
        User user = getUserStored(firstId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    public User getByIdUser(final String expectedId) {
        return getUserStored(expectedId);
    }

    public Collection<User> getCommonFriendsList(final String firstId, final String secondId) {
        User user = getUserStored(firstId);
        User otherUser = getUserStored(secondId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }

    private void validate(final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Не задано name. Установлено значение {} из login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Name не содержит буквенных символов. " +
                    "Установлено значение {} из login", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации Пользователя: " + messageBuilder, violations);
        }
        if (user.getId() == 0) {
            user.setId(counter++);
        }
    }
}