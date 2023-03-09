package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findById(Integer id);
    Collection<Genre> findAll();
}
