package ru.otus.service;

import ru.otus.model.Role;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> get(Long id);

    Optional<User> findByUsername(String username);

    List<User> getAll();

    List<User> getByRole(Role role);
}