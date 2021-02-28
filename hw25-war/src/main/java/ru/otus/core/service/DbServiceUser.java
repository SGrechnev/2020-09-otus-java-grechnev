package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUser {

    String save(User user);

    Optional<User> get(String login);

    List<User> getAll();

    Optional<User> getRandom();

}
