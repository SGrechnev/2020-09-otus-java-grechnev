package ru.otus.core.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class ClientDaoImpl implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoImpl.class);

    private final JdbcMapper<Client> jdbcMapper;
    private final SessionManager sessionManager;

    public ClientDaoImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<Client> dbExecutor) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, Client.class);
    }

    @Override
    public Optional<Client> findById(long id) {
        return Optional.ofNullable(this.jdbcMapper.findById(id, Client.class));
    }

    @Override
    public long insert(Client client) {
        this.jdbcMapper.insert(client);
        return client.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }
}
