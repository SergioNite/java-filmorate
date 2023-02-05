package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);
    Optional<Film> updateFilm(long filmId, Film film);
    void deleteFilm(long filmId);
    Optional<Film> getFilmById(long filmId);
    Film addLike(User user, Film film);
    Film deleteLike(User user, Film film);
    Collection<Film> getAll();
    List<Film> getPopularFilms(int count);
}
