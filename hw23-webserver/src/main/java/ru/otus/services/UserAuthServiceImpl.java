package ru.otus.services;

import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DbServiceUser dbServiceUser;

    public final static String ADMIN_ROLE = "ADMIN_ROLE";

    public UserAuthServiceImpl(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
        User adminUser = new User("admin", "Trevor", "strong-password", User.Role.ADMIN);
        this.dbServiceUser.saveUser(adminUser);
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceUser.getUser(login)
                .map(user -> user.getPassword().equals(password) && user.getRole().equals(User.Role.ADMIN))
                .orElse(false);
    }

}
