package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.List;
import java.util.Optional;

public class InMemoryMpaStorage implements MpaStorage{
    @Override
    public Optional<Mpaa> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Mpaa> getAll() {
        return null;
    }
}
