package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmController filmController;
    ValidationException validationException;
    InternalException internalException;

    @Test
    void ThrowAnExceptionIfTheMovieNameIsNull() {
        Film film = new Film(null, "Описание фильма",
                LocalDate.of(2022, 11, 5), 120);
        assertThrows(
                NullPointerException.class,
                () -> filmController.createFilms(film));
    }

    @Test
    void ThrowAnExceptionIfTheMovieDescriptionIsNull() {
        Film film = new Film("Фильм", null,
                LocalDate.of(2022, 11, 5), 120);
        assertThrows(
                NullPointerException.class,
                () -> filmController.createFilms(film));
    }

    @Test
    void ThrowAnExceptionIfTheMovieReleaseDateIsNull() {
        Film film = new Film("Фильм", "Описание фильма",
                null, 120);
        assertThrows(
                NullPointerException.class,
                () -> filmController.createFilms(film));
    }

    @Test
    void ThrowAnExceptionIfTheMovieDurationIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> filmController.createFilms(null));
    }

    @Test
    void ThrowAnExceptionWhenAddingMovieWithReleaseDateBeforeDecember281985() {
        Film film = new Film("Фильм", "Описание фильма",
                LocalDate.of(1895, 12, 27), 100);
        validationException = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Дата релиза фильма не может быть ранее 28.12.1895.",
                validationException.getMessage());
    }

    @Test
    void creatingMovieWith200CharacterDescription() {
        Film film = new Film("Титаник", "Американский фильм-катастрофа 1997 года, снятый режиссёром " +
                "Джеймсом Кэмероном, в котором показана гибель легендарного лайнера «Титаник». Герои фильма, будучи " +
                "представителями различных социальных слоёв",
                LocalDate.of(2022, 11, 5), 120);
        filmController.createFilms(film);
        Assertions.assertEquals(200, film.getDescription().length());
        System.out.println(film.getDescription().length());
    }

    @Test
    void creatingMovieWith1CharacterDescription() {
        Film film = new Film("Титаник-2", "А",
                LocalDate.of(2022, 11, 5), 120);
        filmController.createFilms(film);
        Assertions.assertEquals(1, film.getDescription().length());
        System.out.println(film.getDescription().length());
    }

    @Test
    void throwAnExceptionIfTheMovieDurationIs0() {
        Film film = new Film("Фильм", "Описание фильма",
                LocalDate.of(2022, 11, 5), 0);
        internalException = assertThrows(
                InternalException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",
                internalException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheMovieDurationIsNegative() {
        Film film = new Film("Фильм", "Описание фильма",
                LocalDate.of(2022, 11, 5), -100);
        internalException = assertThrows(
                InternalException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",
                internalException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheMovieNameIsEmpty() {
        Film film = new Film("", "Описание фильма",
                LocalDate.of(2022, 11, 5), 120);
        validationException = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Название фильма не может быть пустым или состоять из пробелов.",
                validationException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheMovieNameConsistsOfSpaces() {
        Film film = new Film("   ", "Описание фильма",
                LocalDate.of(2021, 11, 5), 120);
        validationException = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Название фильма не может быть пустым или состоять из пробелов.",
                validationException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheMovieDescriptionIsEmpty() {
        Film film = new Film("Фильм", "",
                LocalDate.of(2022, 11, 5), 120);
        validationException = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Описание фильма не может быть пустым или состоять из пробелов.",
                validationException.getMessage());
    }

    @Test
    void throwAnExceptionIfTheMovieDescriptionConsistsOfSpaces() {
        Film film = new Film("Фильм", "     ",
                LocalDate.of(2022, 11, 5), 120);
        validationException = assertThrows(
                ValidationException.class,
                () -> filmController.createFilms(film));
        Assertions.assertEquals("Описание фильма не может быть пустым или состоять из пробелов.",
                validationException.getMessage());
    }
}