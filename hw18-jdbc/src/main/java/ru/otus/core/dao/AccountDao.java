package ru.otus.core.dao;

import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(String no);
    //List<Account> findAll();

    String insert(Account account);

    //void update(Account account);
    //String insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
