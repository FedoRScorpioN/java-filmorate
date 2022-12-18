package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    ValidationException validationException;

    @Test
    void throwAnExceptionIfTheEmailFieldIsEmpty() {
        User user = new User("", "Fedor", LocalDate.of(1988, 11, 22));
        validationException = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));
        Assertions.assertEquals("Почта не может быть пустой и содержать пробелы, а символ @ обязателен.",
                validationException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheEmailFieldIsContainsSpaces() {
        User user = new User("     ", "Fedor", LocalDate.of(1988, 11, 22));
        validationException = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));
        Assertions.assertEquals("Почта не может быть пустой и содержать пробелы, а символ @ обязателен.",
                validationException.getMessage());
    }

    @Test
    void RaiseAnExceptionIfTheLoginFieldIsEmpty() {
        User user = new User("zapevalov@yandex.ru", "", LocalDate.of(1988, 11, 22));
        validationException = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                validationException.getMessage());
    }

    @Test
    void RaiseAnExceptionIfTheLoginFieldIsContainsSpaces() {
        User user = new User("zapevalov@yandex.ru", "   ", LocalDate.of(1988, 11, 22));
        validationException = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                validationException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheUsersDateOfBirthIsInTheFuture() {
        User user = new User("zapevalov@yandex.ru", "Fedor", LocalDate.of(2100, 1, 1));
        validationException = assertThrows(
                ValidationException.class,
                () -> userController.createUser(user));
        Assertions.assertEquals("Дата рождения не может быть в будущем.",
                validationException.getMessage());
    }
}