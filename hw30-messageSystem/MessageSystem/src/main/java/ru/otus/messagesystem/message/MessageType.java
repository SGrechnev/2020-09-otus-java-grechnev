package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData"),
    GET_USER("GetUser"),
    GET_ALL("GetAll"),
    GET_RANDOM("GetRandom"),
    SAVE_USER("SaveUser");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
