package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserStorageTest {
    private final UserDbStorage userStorage;

    private User firstUser = new User(1,
            "1@ya.ru",
            "login1",
            "Name1",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());
    private User secondUser = new User(2,
            "2@ya.ru",
            "login2",
            "Name2",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());
    private User ThirdUser = new User(3,
            "3@ya.ru",
            "login3",
            "Name3",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());

    @Test
    public void createUserTest() {
        userStorage.createUser(firstUser);
        AssertionsForClassTypes.assertThat(firstUser).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(firstUser).extracting("name").isNotNull();
    }

    @Test
    public void getAllUsersTest() {
        userStorage.createUser(secondUser);
        userStorage.createUser(ThirdUser);
        Collection<User> dbUsers = userStorage.findAllUsers();
        assertEquals(2, dbUsers.size());
    }

    @Test
    public void deleteUserTest() {
        Collection<User> beforeDelete = userStorage.findAllUsers();
        userStorage.deleteUser(firstUser);
        Collection<User> afterDelete = userStorage.findAllUsers();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}