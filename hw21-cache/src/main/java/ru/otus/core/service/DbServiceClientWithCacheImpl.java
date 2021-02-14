package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceClientWithCacheImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientWithCacheImpl.class);

    private final MyCache<String, Client> clientCache;
    private final ClientDao clientDao;

    public DbServiceClientWithCacheImpl(MyCache<String, Client> clientCache, ClientDao clientDao) {
        if(clientCache == null){
            throw new IllegalArgumentException("clientCache must be not null");
        }
        if(clientDao == null){
            throw new IllegalArgumentException("clientDao must be not null");
        }
        this.clientCache = clientCache;
        this.clientDao = clientDao;
    }

    @Override
    public long saveClient(Client client) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long clientId = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();
                clientCache.put(String.valueOf(clientId), client);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client requestedClient = clientCache.get(String.valueOf(id));
        if (requestedClient != null) {
            return Optional.of(requestedClient);
        }
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> optionalClient = clientDao.findById(id);
                optionalClient.ifPresent(
                        client -> clientCache.put(String.valueOf(id), client)
                );
                return optionalClient;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}