package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.User;

public class UserData extends ResultDataType {

    User user;

    public UserData() {
    }

    public UserData(User user) {
        this.user = user;
    }

    public User get() {
        return user;
    }

    public void set(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserData{" + user.toString() + "}";
    }
}
