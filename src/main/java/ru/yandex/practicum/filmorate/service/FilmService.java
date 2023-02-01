package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId).get();
        User user = userStorage.getUserById(userId).get();
        if (Objects.isNull(film) || Objects.isNull(user)) {
            throw new NotFoundException("Передан некорректный ID фильма или пользователя");
        }
        film.addLike(userId);
        filmStorage.updateFilm(film.getId(), film);
        log.info("User {} set like mark to the film {}", user, film);
    }

    public void deleteLike(Long filmId, Long userId) throws NotFoundException {
        if (filmId < 1 || userId < 1) {
            throw new NotFoundException("Передан некорректный ID фильма или пользователя");
        }
        Film film = filmStorage.getFilmById(filmId).get();
        User user = userStorage.getUserById(userId).get();
        if (Objects.isNull(film) || Objects.isNull(user)) {
            throw new NotFoundException("Передан некорректный ID фильма или пользователя");
        }
        film.deleteLike(userId);
        filmStorage.updateFilm(film.getId(), film);
        log.info("User {} delete like from the film {}", user, film);
    }

    public List<Film> getPopularFilms(@Positive int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId).orElseThrow(
                () -> new NotFoundException(String.format("Не удалось найти фильм по ID %s", filmId))
        );
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(long filmId, Film film) {
        return filmStorage.updateFilm(filmId, film).orElseThrow(
                () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Не удалось обновить данные фильма " + film.getId())
        );
    }

    public void deleteFilm(long filmId) {
        filmStorage.deleteFilm(filmId);
    }
}
