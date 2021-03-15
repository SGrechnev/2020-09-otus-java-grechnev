package ru.otus.front;

import ru.otus.dto.UserData;
import ru.otus.dto.UserDataList;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.model.User;

public interface FrontendService {

    void get(String userLogin, MessageCallback<UserData> userConsumer);

    void getAll(MessageCallback<UserDataList> userConsumer);

    void save(User user, MessageCallback<UserData> userConsumer);

    void getRandom(MessageCallback<UserData> userConsumer);
}

