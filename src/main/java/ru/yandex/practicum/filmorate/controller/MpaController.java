package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.service.MpaServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaServiceImpl mpaService;

    @Autowired
    public MpaController(MpaServiceImpl mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("{id}")
    public Optional<Mpaa> getMpaById(@PathVariable Integer id) {
        return mpaService.getById(id);
    }

    @GetMapping()
    public List<Mpaa> getAll() {
        return mpaService.getAll();
    }
}
