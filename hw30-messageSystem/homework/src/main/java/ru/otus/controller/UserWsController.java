package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.model.User;
import ru.otus.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserWsController {
    private static final Logger logger = LoggerFactory.getLogger(UserWsController.class);

    private final SimpMessagingTemplate template;
    private final List<String> ids = new ArrayList<>();

    @Autowired
    private final UserService userService;

    public UserWsController(SimpMessagingTemplate template, UserService userService) {
        this.template = template;
        this.userService = userService;
    }

    @MessageMapping("/getUsers.{id}")
    @SendTo("/topic/users.{id}")
    public List<User> getUsers(@DestinationVariable String id) {
        logger.info("get all users");
        logger.info("not so fast...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ids.add(id);
        return userService.getAll();
    }

    @MessageMapping("/saveUser.{id}")
    @SendTo("/topic/savedUser.{id}")
    public User createUser(User user) {
        logger.info("save user {}", user);
        userService.save(user);
        logger.info("user {} saved", user);
        ids.forEach(id_ -> template.convertAndSend("/topic/users." + id_, user));
        return user;
    }

    @MessageMapping("/getUser.{id}")
    @SendTo("/topic/getUser.{id}")
    public Optional<User> getUser(String login) {
        var optionalUser = userService.get(login);
        logger.info("get user by login: {}", optionalUser.orElse(null));
        return optionalUser;
    }

    @MessageMapping("/getRandomUser.{id}")
    @SendTo("/topic/getUser.{id}")
    public Optional<User> getRandomUser() {
        var optionalUser = userService.getRandom();
        logger.info("get random user: {}", optionalUser.orElse(null));
        return optionalUser;
    }

    @MessageExceptionHandler()
    private String handleMessageException(Exception e) {
        return "Oops! Something went wrong. Error: " + e.getClass().getSimpleName();
    }
}