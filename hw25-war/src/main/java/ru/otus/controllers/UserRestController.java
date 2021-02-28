package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUser;

@RestController
public class UserRestController {

    private final DbServiceUser userService;

    public UserRestController(DbServiceUser userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public User getUserByName(@RequestParam(name = "login") String login) {
        return userService.get(login).orElse(null);
    }

    @PostMapping("/api/user")
    public String saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/random")
    public User findRandomUser() {
        return userService.getRandom().orElse(null);
    }

}
