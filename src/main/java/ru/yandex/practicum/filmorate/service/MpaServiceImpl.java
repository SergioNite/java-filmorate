package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Optional<Mpaa> getById(Integer id) {
        return Optional.ofNullable(mpaStorage.getById(id).orElseThrow(
                () -> new NotFoundException(String.format("Не удалось найти рейтинг MPA по ID %s", id))
        ));
    }

    public List<Mpaa> getAll() {
        return mpaStorage.getAll();
    }
}
