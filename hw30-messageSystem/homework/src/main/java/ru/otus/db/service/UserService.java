package ru.otus.db.service;

import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> get(String login);

    List<User> getAll();

    Optional<User> getRandom();
}
