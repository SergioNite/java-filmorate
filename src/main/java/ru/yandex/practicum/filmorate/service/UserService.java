package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User createUser(User user);
    User updateUser(long id, User user);
    User deleteUser(long id);
    User getUserById(long userId);
    Collection<User> getAll();
    List<User> addFriend(long id, long friendId);
    List<User> deleteFriend(long id, long friendId);
    List<User> getFriends(Long id);
    List<User> getMutualFriends(Long id, Long otherId);

}
