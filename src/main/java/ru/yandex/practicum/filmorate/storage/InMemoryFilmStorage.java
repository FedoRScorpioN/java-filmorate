package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private static final LocalDate DATE_FROM = LocalDate.of(1895, 12, 28);
    protected final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilms(Film film) {
        validationFilms(film);
        checkFilms(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {} добавлен в Вашу коллекцию", film.getName());
        return film;
    }

    @Override
    public Film updateFilms(Film film) {
        validationFilms(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Такой фильм отсутствует в коллекции. Невозможно обновить данные.");
        }
        films.put(film.getId(), film);
        log.info("Данные о фильме: {} успешно обновлены", film.getName());
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    protected void validationFilms(Film film) {
        if (film.getReleaseDate().isBefore(DATE_FROM)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть ранее 28.12.1895.");
        }
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Название фильма пустое: {}.", film);
            throw new ValidationException("Название фильма не может быть пустым или состоять из пробелов.");
        }
        if (film.getDescription().isBlank() || film.getDescription().isEmpty()) {
            log.warn("Описание фильма: {} не заполнено.", film);
            throw new ValidationException("Описание фильма не может быть пустым или состоять из пробелов.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Текущее описание фильма: {}", film.getDescription());
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
    }

    private void checkFilms(Film filmToAdd) {
        films.values().stream()
                .filter(film -> isFilmAlreadyExists(filmToAdd, film))
                .forEach(film -> {
                    log.warn("Фильм к добавлению: {} уже существует", filmToAdd);
                    throw new ValidationException("В нашей коллекции уже есть такой фильм.");
                });
    }

    private boolean isFilmAlreadyExists(Film filmToAdd, Film film) {
        return filmToAdd.getName().equals(film.getName()) &&
                filmToAdd.getReleaseDate().equals(film.getReleaseDate());
    }

    @Override
    public Film getByIdFilms(int id) {
        return films.get(id);
    }

    @Override
    public Film deleteByIdFilms(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }
}