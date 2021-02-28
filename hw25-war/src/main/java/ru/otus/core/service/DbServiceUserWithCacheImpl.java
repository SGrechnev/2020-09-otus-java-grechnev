package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.cache.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DbServiceUserWithCacheImpl implements DbServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserWithCacheImpl.class);

    private final HwCache<String, User> userCache;
    private final UserDao userDao;
    private final Random r = new Random();

    public DbServiceUserWithCacheImpl(HwCache<String, User> userCache, UserDao userDao) {
        if (userCache == null) {
            throw new IllegalArgumentException("clientCache must be not null");
        }
        if (userDao == null) {
            throw new IllegalArgumentException("userDao must be not null");
        }
        this.userCache = userCache;
        this.userDao = userDao;
    }

    @Override
    public String save(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                String login = userDao.insertOrUpdate(user);
                sessionManager.commitSession();
                userCache.put(String.valueOf(login), user);
                return login;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> get(String login) {
        User requestedUser = userCache.get(String.valueOf(login));
        if (requestedUser != null) {
            return Optional.of(requestedUser);
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> optionalClient = userDao.findByLogin(login);
                optionalClient.ifPresent(
                        client -> userCache.put(String.valueOf(login), client)
                );
                return optionalClient;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> users = userDao.findAll();
                for (var user : users) {
                    userCache.put(String.valueOf(user.getLogin()), user);
                }
                return users;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<User> getRandom() {
        List<User> users = getAll();
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(r.nextInt(users.size())));
    }
}