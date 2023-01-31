package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private Long currentMaxId = 1L;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        if (user.getId() == null){
            user.setId(currentMaxId++);
            users.put(user.getId(),user);
            log.info("createUser: {}", user);
        }
        return users.get(user.getId());
    }

    @Override
    public Optional<User> updateUser(long userId, User user) {

        if (user.getId() != null && user.getId() < 1) {
            return Optional.empty();
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(userId) && userId == user.getId()){
            users.put(user.getId(),user);
            return Optional.of(users.get(user.getId()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUser(long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(long userId) {
        User user = users.get(userId);
        if (Objects.isNull(user)){
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public List<User> getFriends(long userId) {
        List<User> result = new ArrayList<>();
        Set<Long> friendSet = users.get(userId).getFriends();
        friendSet.forEach(j ->result.add(getUserById(j).get()));
        return result;
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> result = new ArrayList<>();
        Optional<User> localUser = getUserById(userId);
        Optional<User> friendUser = getUserById(friendId);
        if (localUser.isEmpty() || friendUser.isEmpty()){
            return result;
        }
        localUser.get().getFriends().stream()
                .filter(friendUser.get().getFriends()::contains)
                .collect(Collectors.toSet())
                .forEach(j->result.add(getUserById(j).get()));
        return result;
    }

}
