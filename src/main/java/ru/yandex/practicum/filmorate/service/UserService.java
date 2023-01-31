package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }
    public User createUser(User user) {
        return userStorage.addUser(user);
    }
    public User updateUser(long id, User user) {
        return userStorage.updateUser(id, user).orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "Cannot find user"));
    }

    public User deleteUser(@PathVariable long id) {
        return userStorage.deleteUser(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cannot find user"));
    }

    public Optional<User> getUserById(long userId){
        return userStorage.getUserById(userId);
    }

    public Collection<User> getAll(){
        return userStorage.getAll();
    }

    public Optional<User> addFriend(long id, long friendId){
        User user = userStorage.getUserById(id).get();
        User friend = userStorage.getUserById(friendId).get();
        if (Objects.isNull(user) || Objects.isNull(friend)){
            return Optional.empty();
        }
        user.addFriend(friend);
        friend.addFriend(user);
        return Optional.of(user);
    }
    public void deleteFriend(long id, long friendId){
        User user = userStorage.getUserById(id).get();
        User friend = userStorage.getUserById(friendId).get();
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }

    public List<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }
    public List<User> getMutualFriends(Long id, Long otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }
}
