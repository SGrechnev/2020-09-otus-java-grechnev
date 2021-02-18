package ru.otus.servlet;

public class ApiResponse {
    private final Status status;
    private final String message;

    public ApiResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public enum Status {
        SUCCESS,
        ERROR
    }
}
