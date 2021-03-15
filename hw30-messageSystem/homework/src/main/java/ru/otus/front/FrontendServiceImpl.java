package ru.otus.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dto.UserData;
import ru.otus.dto.UserDataList;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.model.User;


public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void get(String login, MessageCallback<UserData> userConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(new User(login)),
                MessageType.GET_USER, userConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getAll(MessageCallback<UserDataList> userConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserDataList(),
                MessageType.GET_ALL, userConsumer);
        logger.info("getAll::outMsg: {}", outMsg);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void save(User user, MessageCallback<UserData> userConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(user),
                MessageType.SAVE_USER, userConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getRandom(MessageCallback<UserData> userConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(),
                MessageType.GET_RANDOM, userConsumer);
        msClient.sendMessage(outMsg);
    }
}
