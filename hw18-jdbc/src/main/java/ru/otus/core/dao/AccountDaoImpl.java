package ru.otus.core.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoImpl implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final JdbcMapper<Account> jdbcMapper;
    private final SessionManager sessionManager;

    public AccountDaoImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<Account> dbExecutor) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, Account.class);
    }

    @Override
    public Optional<Account> findById(String no) {
        return Optional.ofNullable(this.jdbcMapper.findById(no, Account.class));
    }

    @Override
    public String insert(Account account) {
        this.jdbcMapper.insert(account);
        return account.getNo();
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }
}
