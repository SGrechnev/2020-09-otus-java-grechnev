package ru.otus.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.db.repository.UserRepository;
import ru.otus.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Map<String, User> cache;

    public User save(User user) {
        cache.put(user.getLogin(), user);
        return userRepository.save(user);
    }

    public Optional<User> get(String login) {
        User cachedUser = cache.get(login);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }
        var user = userRepository.findById(login);
        user.ifPresent((u) -> cache.put(login, u));
        return user;
    }

    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> cache.put(user.getLogin(), user));
        return users;
    }

    public Optional<User> getRandom() {
        var users = userRepository.findAll();
        if (users.size() == 0) {
            return Optional.empty();
        }
        var r = new Random();
        return Optional.of(users.get(r.nextInt(users.size())));
    }
}
