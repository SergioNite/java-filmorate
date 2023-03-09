package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.List;
import java.util.Optional;

public interface MpaService {
    Optional<Mpaa> getById(Integer id);
    List<Mpaa> getAll();
}
