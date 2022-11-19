package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        log.info("Сформирован список пользователей.");
        return userStorage.findAllUsers();
    }

    public User getByIdUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        log.info("Пользователь под номером: '{}' отправлен.", id);
        return userStorage.getByIdUser(id);
    }

    public User deleteByIdUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Невозможно удалить пользователя, так как он не существует.");
        }
        log.info("Пользователь под номером: '{}' удален", id);
        return userStorage.deleteByIdUser(id);
    }

    public List<User> addFriends(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new NotFoundException(String.format("Пользователя под номером: %d или под номером: %d" +
                    "не существует.", firstId, secondId));
        }
        if (userStorage.getByIdUser(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи уже являются друзьями.");
        }
        userStorage.getByIdUser(firstId).getFriends().add(secondId);
        userStorage.getByIdUser(secondId).getFriends().add(firstId);
        log.info("Пользователи: '{}' и '{}' теперь Друзья.",
                userStorage.getByIdUser(firstId).getName(),
                userStorage.getByIdUser(secondId).getName());
        return Arrays.asList(userStorage.getByIdUser(firstId), userStorage.getByIdUser(secondId));
    }

    public List<User> removeFriends(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new NotFoundException(String.format("Пользователя под номером: %d или под номером: %d" +
                    "не существует.", firstId, secondId));
        }
        if (!userStorage.getByIdUser(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи не являются друзьями.");
        }
        userStorage.getByIdUser(firstId).getFriends().remove(secondId);
        userStorage.getByIdUser(secondId).getFriends().remove(firstId);
        log.info("Пользователи: '{}' и '{}' перестали дружить.",
                userStorage.getByIdUser(firstId).getName(),
                userStorage.getByIdUser(secondId).getName());
        return Arrays.asList(userStorage.getByIdUser(firstId), userStorage.getByIdUser(secondId));
    }

    public List<User> getFriendsListById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователя не существует. Невозможно получить список его друзей.");
        }
        log.info("Получен список друзей пользователя", userStorage.getByIdUser(id).getName());
        return userStorage.getByIdUser(id).getFriends().stream()
                .map(userStorage::getByIdUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        if (userStorage.getByIdUser(firstId) == null || userStorage.getByIdUser(secondId) == null) {
            throw new NotFoundException(String.format("Пользователь под номером: %d или под номером: %d" +
                    "не существует.", firstId, secondId));
        }
        User firstUser = userStorage.getByIdUser(firstId);
        User secondUser = userStorage.getByIdUser(secondId);
        log.info("Создан список общих друзей пользователей: '{}' и '{}'.",
                firstUser.getName(), secondUser.getName());
        return firstUser.getFriends().stream()
                .filter(friendId -> secondUser.getFriends().contains(friendId))
                .map(userStorage::getByIdUser)
                .collect(Collectors.toList());
    }
}