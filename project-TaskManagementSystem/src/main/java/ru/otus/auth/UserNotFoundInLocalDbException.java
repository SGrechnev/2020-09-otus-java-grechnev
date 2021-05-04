package ru.otus.auth;

import org.springframework.security.authentication.AccountStatusException;

public class UserNotFoundInLocalDbException extends AccountStatusException {
    public UserNotFoundInLocalDbException(String msg) {
        super(msg);
    }

    public UserNotFoundInLocalDbException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
