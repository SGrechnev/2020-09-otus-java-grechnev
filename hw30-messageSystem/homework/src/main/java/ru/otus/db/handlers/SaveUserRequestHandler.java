package ru.otus.db.handlers;

import ru.otus.db.service.UserService;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.model.User;

import java.util.Optional;

public class SaveUserRequestHandler implements RequestHandler<UserData> {

    private final UserService userService;

    public SaveUserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserData requestUserData = MessageHelper.getPayload(msg);
        User savedUser = userService.save(requestUserData.get());
        UserData responseUserData = new UserData(savedUser);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, responseUserData));
    }
}