package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User getByIdUser(int id);

    User deleteByIdUser(int id);

    Collection<User> findAllUsers();

    Map<Integer, User> getUsers();

}