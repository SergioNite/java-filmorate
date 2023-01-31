package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Long currentMaxId = 1L;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        Film result =  filmService.addFilm(film);
        return result;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Optional<Film> result = filmService.updateFilm(film.getId(),film);
        if (result.isEmpty()){
            throw new ValidationException("Не удалось обновить данные фильма "+film.getId());
        }
        return result.get();
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }
    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count){
        return filmService.getPopularFilms(count);
    }
    @GetMapping("{id}")
    public Optional<Film> getFilmById(@PathVariable long id) {
        Optional<Film> result = filmService.getFilmById(id);
        if (result.isEmpty()){
            throw new NotFoundException(String.format("Не удалось найти фильм по ID %s",id));
        }
        return result;
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id,userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id,userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }
}
