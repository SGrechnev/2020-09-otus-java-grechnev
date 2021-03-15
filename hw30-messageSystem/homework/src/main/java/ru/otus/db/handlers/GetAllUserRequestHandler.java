package ru.otus.db.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.service.UserService;
import ru.otus.dto.UserData;
import ru.otus.dto.UserDataList;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetAllUserRequestHandler implements RequestHandler<UserDataList> {

    private static final Logger logger = LoggerFactory.getLogger(GetAllUserRequestHandler.class);

    private final UserService userService;

    public GetAllUserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        List<User> responseUser = userService.getAll();
        UserDataList userDataList = new UserDataList(responseUser.stream()
                .map(UserData::new)
                .collect(Collectors.toList())
        );
        Message outMsg = MessageBuilder.buildReplyMessage(msg, userDataList);
        logger.info("outMsg: {}", outMsg);
        return Optional.of(outMsg);
    }
}