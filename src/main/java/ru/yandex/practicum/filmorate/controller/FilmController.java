package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private static final LocalDate DATE_FROM = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        validationUsers(film);
        checkUsers(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} добавле в Вашу коллекцию", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film film) {
        validationUsers(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такой фильм отсутствует в коллекции. Невозможно обновить данные.");
        }
        checkUsers(film);
        films.put(film.getId(), film);
        log.info("Данные о фильме: {} успешно обновлены", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    private void validationUsers(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE_FROM)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть ранее 28.12.1895.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Текущее описание фильма: {}", film.getDescription());
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
    }

    private void checkUsers(@RequestBody Film filmToAdd) {
        boolean exists = films.values().stream()
                .anyMatch(film -> alreadyExistsUsers(filmToAdd, film));
        if (exists) {
            log.warn("Фильм к добавлению: {}", filmToAdd);
            throw new ValidationException("В нашей коллекции уже есть такой фильм.");
        }
    }

    private boolean alreadyExistsUsers(Film filmToAdd, Film film) {
        return filmToAdd.getName().equals(film.getName()) &&
                filmToAdd.getReleaseDate().equals(film.getReleaseDate());
    }
}
