package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Long currentMaxId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<User> createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        if (user.getId() == null){
            user.setId(currentMaxId++);
            users.put(user.getId(),user);
            log.info("createUser {}",user);
        } else {
            throw new ValidationException("Wrong user ID="+user.getId()+" Users="+users.toString());
        }
        return Optional.of(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() != null && user.getId() < 1) {
            throw new ValidationException("Wrong user ID");
        }
        if (users.containsKey(user.getId())){
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(),user);
            log.info("updateUser {}", user);
        } else {
            throw new ValidationException("Wrong user userID="+user.getId());
        }

        return user;
    }
    @GetMapping
    public Collection<User> getAll(){
        return users.values();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }
}
