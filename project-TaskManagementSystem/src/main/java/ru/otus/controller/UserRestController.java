package ru.otus.controller;

import org.springframework.web.bind.annotation.*;
import ru.otus.model.User;
import ru.otus.service.UserService;

import java.util.List;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable(name = "id") Long id) {
        return userService.get(id).orElse(null);
    }

    @PostMapping("/api/users")
    public User saveUser(@RequestBody User user) {
        return userService.save(user);
    }

}