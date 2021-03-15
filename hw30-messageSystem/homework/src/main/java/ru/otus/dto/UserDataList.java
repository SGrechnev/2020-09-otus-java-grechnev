package ru.otus.dto;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDataList extends ResultDataType {

    private final List<UserData> userDataList;

    public UserDataList() {
        this.userDataList = new ArrayList<>();
    }

    public UserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public List<UserData> get() {
        return List.copyOf(userDataList);
    }

    public List<User> toUserList() {
        return userDataList.stream()
                .map(UserData::get)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "UserDataList{" + userDataList.toString() + "}";
    }
}
