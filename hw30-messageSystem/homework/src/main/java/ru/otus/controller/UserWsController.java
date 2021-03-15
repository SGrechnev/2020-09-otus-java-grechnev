package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.front.FrontendService;
import ru.otus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserWsController {
    private static final Logger logger = LoggerFactory.getLogger(UserWsController.class);

    private final SimpMessagingTemplate template;
    private final List<String> ids = new ArrayList<>();

    @Autowired
    private final FrontendService frontendService;

    public UserWsController(SimpMessagingTemplate template, FrontendService frontendService) {
        this.template = template;
        this.frontendService = frontendService;
    }

    @MessageMapping("/getUsers.{id}")
    public void getUsers(@DestinationVariable String id) {
        logger.info("get all users");
        ids.add(id);
        frontendService.getAll(userList -> {
            logger.debug("{} users received", userList.get().size());
            template.convertAndSend("/topic/users." + id, userList.toUserList());
        });
    }

    @MessageMapping("/saveUser.{id}")
    public void createUser(User user, @DestinationVariable String id) {
        logger.info("save user {}", user);
        frontendService.save(user, (u) -> {
            logger.debug("user saved {}", u.get());
            template.convertAndSend("/topic/savedUser." + id, u.get());
            ids.forEach(id_ -> template.convertAndSend("/topic/users." + id_, u.get()));
        });
    }

    @MessageMapping("/getUser.{id}")
    public void getUser(String login, @DestinationVariable String id) {
        frontendService.get(login, userData -> {
            template.convertAndSend("/topic/getUser." + id, Optional.ofNullable(userData.get()));
        });
    }

    @MessageMapping("/getRandomUser.{id}")
    public void getRandomUser(@DestinationVariable String id) {
        frontendService.getRandom(userData -> {
            template.convertAndSend("/topic/getUser." + id, Optional.ofNullable(userData.get()));
        });
    }

    @MessageExceptionHandler()
    private String handleMessageException(Exception e) {
        return "Oops! Something went wrong. Error: " + e.getClass().getSimpleName();
    }
}