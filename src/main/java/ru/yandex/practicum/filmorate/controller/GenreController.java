package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreServiceImpl service;

    @Autowired
    public GenreController(GenreServiceImpl service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Optional<Genre> getGenre(@PathVariable Integer id) {
        return Optional.ofNullable(service.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Не удалось найти рейтинг MPA по ID %s", id))
        ));
    }

    @GetMapping
    public Collection<Genre> findAll() {
        return service.findAll();
    }
}
