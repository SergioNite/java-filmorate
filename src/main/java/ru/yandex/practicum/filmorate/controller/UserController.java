package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        if (user.getId() != null && user.getId() < 1) {
            throw new ValidationException("createUser: Wrong user ID="+user.getId());
        }
        return userService.createUser(user);
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() != null && user.getId() < 1) {
            throw new ValidationException("Wrong user ID");
        }
        return userService.updateUser(user.getId(),user);
    }
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);
    }
    @GetMapping("{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()){
            throw new ResponseStatusException(NOT_FOUND, "Unable to find user");
        }
        return user;
    }
    @GetMapping
    public Collection<User> getAll(){
        return userService.getAll();
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Ошибка в ID пользователей!");
        }
        Optional<User> result = userService.addFriend(id,friendId);
        if (result.isEmpty()){
            throw new ValidationException("Системная ошибка: Данные не записаны!");
        }
        return result.get();
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Set<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCrossFriend(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }


}
