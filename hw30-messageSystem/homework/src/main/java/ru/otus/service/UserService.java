package ru.otus.service;

import ru.otus.model.User;

import java.util.*;

public interface UserService {

    User save(User user);

    Optional<User> get(String login);

    List<User> getAll();

    Optional<User> getRandom();
}
