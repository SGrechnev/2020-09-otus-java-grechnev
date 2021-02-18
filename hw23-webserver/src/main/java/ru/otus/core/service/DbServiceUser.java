package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUser {

    String saveUser(User user);

    Optional<User> getUser(String login);

    List<User> getAllUsers();

}
