package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        if (!valid(user)) {
            logger.info("Invalid user: {}", user);
            return null;
        }
        try {
            return userRepository.save(user);
        } catch (Throwable t) {
            logger.warn("t:", t);
            return null;
        }
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getByRole(Role role) {
        return userRepository.findByRole(role);
    }

    private static boolean valid(User user) {
        return user != null &&
                user.getFullname() != null &&
                !user.getFullname().equals("") &&
                user.getUsername() != null &&
                !user.getUsername().equals("");
    }
}