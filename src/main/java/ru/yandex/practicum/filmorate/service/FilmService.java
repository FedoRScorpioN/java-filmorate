package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private static int counter = 1;
    private final Validator validator;
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(Validator validator, FilmStorage filmStorage,
                       @Autowired(required = false) UserService userService) {
        this.validator = validator;
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film createFilms(Film film) {
        validate(film);
        validateReleaseDate(film, "");
        return filmStorage.createFilms(film);
    }

    public Film updateFilms(Film film) {
        return filmStorage.updateFilms(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Сформирован список фильмов.");
        return filmStorage.findAllFilms();
    }

    public Film getByIdFilms(String id) {
        return getFilmStored(id);
    }

    public void addLikeFilms(String filmId, String userId) {
        Film film = getFilmStored(filmId);
        User user = userService.getByIdUser(userId);
        filmStorage.addLike(film.getId(), user.getId());
        log.info("Пользователь под номером: {} лайкнул фильм номер: {}.", userId, filmId);
    }

    public void removeLike(String filmId, String userId) {
        Film film = getFilmStored(filmId);
        User user = userService.getByIdUser(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
        log.info("Пользователь под номером: {} удалил лайк фильму номер: {}.", userId, filmId);
    }

    public Collection<Film> getPopularFilms(String count) {
        log.info("Сформирован список популярных фильмов.");
        Integer size = parseId(count);
        if (size == Integer.MIN_VALUE) {
            size = 10;
        }
        return filmStorage.getPopularFilms(size);
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA)) {
            throw new ValidationException("Дата релиза не может быть раньше: " + START_DATA);
        }
        log.debug("{} фильм: '{}'", text, film.getName());
    }

    private void validate(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<Film> filmConstraintViolation : violations) {
                messageBuilder.append(filmConstraintViolation.getMessage());
            }
            throw new FilmValidationException("Ошибка валидации Фильма: " + messageBuilder, violations);
        }
        if (film.getId() == 0) {
            film.setId(getNextId());
        }
    }

    private static int getNextId() {
        return counter++;
    }

    private Integer parseId(final String expectedId) {
        try {
            return Integer.valueOf(expectedId);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private Film getFilmStored(final String expectedId) {
        final int filmId = parseId(expectedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new NotFoundException("Не удалось найти фильм: '{}'", expectedId);
        }
        Film film = filmStorage.getByIdFilms(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с id: '%d' не найден", filmId));
        }
        return film;
    }
}