package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        return filmService.createFilms(film);
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film film) {
        return filmService.updateFilms(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film getByIdFilms(@PathVariable int id) {
        return filmService.getByIdFilms(id);
    }

    @DeleteMapping("/{id}")
    public Film deleteByIdFilms(@PathVariable int id) {
        return filmService.deleteByIdFilms(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLikeFilms(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLikeFilms(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLikeFilms(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.removeLikeFilms(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}