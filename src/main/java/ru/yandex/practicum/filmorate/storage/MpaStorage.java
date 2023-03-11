package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<Mpaa> getById(int id);

    List<Mpaa> getAll();
}
