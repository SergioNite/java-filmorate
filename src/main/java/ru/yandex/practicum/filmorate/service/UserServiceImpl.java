package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        if (user.getId() != null && user.getId() < 1) {
            throw new ValidationException("CreateUser: Wrong user ID=" + user.getId());
        }
        return userStorage.addUser(user).orElseThrow(
                () -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Cannot create user")
        );
    }

    public User updateUser(long id, User user) {
        if (user.getId() != null && user.getId() < 1) {
            throw new ValidationException("Wrong user ID");
        }
        return userStorage.updateUser(id, user).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cannot find user"));
    }

    public User deleteUser(@PathVariable long id) {
        return userStorage.deleteUser(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cannot find user"));
    }

    public User getUserById(long userId) {
        Optional<User> user = Optional.ofNullable(userStorage.getUserById(userId).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND, "getUserById: Cannot find user")
        ));

        if (!user.isPresent()) {
            throw new ResponseStatusException(NOT_FOUND, "getUserById: Unable to find user");
        }
        return user.get();
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public List<User> addFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Ошибка в ID пользователей!");
        }
        return userStorage.addFriend(id,friendId);
    }

    public List<User> deleteFriend(long id, long friendId) {
        return userStorage.deleteFriend(id,friendId);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }
}
