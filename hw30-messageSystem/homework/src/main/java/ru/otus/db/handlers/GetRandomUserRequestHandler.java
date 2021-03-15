package ru.otus.db.handlers;

import ru.otus.db.service.UserService;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.model.User;

import java.util.Optional;

public class GetRandomUserRequestHandler implements RequestHandler<UserData> {

    private final UserService userService;

    public GetRandomUserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User responseUser = userService.getRandom().orElse(null);
        UserData userData = new UserData(responseUser);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userData));
    }
}