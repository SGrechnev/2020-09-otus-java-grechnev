package ru.otus.db.handlers;

import ru.otus.db.service.UserService;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.model.User;

import java.util.Optional;

public class GetUserRequestHandler implements RequestHandler<UserData> {

    private final UserService userService;

    public GetUserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserData requestUserData = MessageHelper.getPayload(msg);
        User requestUser = requestUserData.get();
        User responseUser = userService.get(requestUser.getLogin()).orElse(null);
        UserData userData = new UserData(responseUser);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userData));
    }
}