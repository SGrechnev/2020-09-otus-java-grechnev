package ru.otus.core.service;

import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.model.Client;

import java.util.Optional;

public interface DBServiceClient {

    long saveClient(Client client);

    Optional<Client> getClient(long id);

    boolean isUseCache();

    void setUseCache(boolean useCache);

    MyCache<Long, Client> getClientCache();

    //List<Client> findAll();
}
