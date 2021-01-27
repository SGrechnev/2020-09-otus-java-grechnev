package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var accountNo = accountDao.insert(account);
                sessionManager.commitSession();

                logger.info("created account: {}", accountNo);
                return accountNo;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(String no) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> accountOptional = accountDao.findById(no);

                logger.info("account: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
