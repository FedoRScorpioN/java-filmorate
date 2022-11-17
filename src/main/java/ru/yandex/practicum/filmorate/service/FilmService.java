package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public Film createFilms(Film film) {
        return filmStorage.createFilms(film);
    }

    public Film updateFilms(Film film) {
        return filmStorage.updateFilms(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Сформирован список фильмов.");
        return filmStorage.findAllFilms();
    }

    public Film getByIdFilms(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден.");
        }
        log.info("Фильм номер: " + id + " отправлен.");
        return filmStorage.getByIdFilms(id);
    }

    public Film deleteByIdFilms(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Невозможно удалить фильм, так как его не в коллекции.");
        }
        log.info("Фильм номер: " + id + " удалён");
        return filmStorage.deleteByIdFilms(id);
    }

    public Film addLikeFilms(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден.");
        }
        filmStorage.getByIdFilms(filmId).getUserLikes().add(userId);
        log.info("Пользователь под номером: {} лайкнул фильм номер: {}.", userId, filmId);
        return filmStorage.getByIdFilms(filmId);
    }

    public Film removeLikeFilms(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден.");
        }
        if (!filmStorage.getByIdFilms(filmId).getUserLikes().contains(userId)) {
            throw new NotFoundException("Отсутствует лайк от пользователя");
        }
        filmStorage.getByIdFilms(filmId).getUserLikes().contains(userId);
        log.info("Пользователь под номером: {} удалил лайк фильму номер: {}.", userId, filmId);
        return filmStorage.getByIdFilms(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Сформирован список популярных фильмов.");
        return filmStorage.findAllFilms().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUserLikes().size(), o1.getUserLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}