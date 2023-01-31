package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);
    Optional<User> updateUser(long userId, User user);
    Optional<User> deleteUser(long userId);
    Optional<User> getUserById(long userId);
    Collection<User> getAll();
    List<User> getFriends(long userId);
    List<User> getMutualFriends(Long userId, Long friendId);
}
