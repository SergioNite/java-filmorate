package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> findById(Integer id){
        return genreStorage.findById(id);
    }

    public Collection<Genre> findAll(){
        return genreStorage.findAll();
    }

}
