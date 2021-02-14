package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.core.service.DbServiceClientWithCacheImpl;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.*;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        logger.info("=================== DEMO SPEED ===================");
        demoSpeed();
        logger.info("=================== DEMO  FREE ===================");
        demoFree();
    }

    public static void demoSpeed() {
        var clientDao = prepareClientDao();

        var dbServiceClientWithoutCache = new DbServiceClientImpl(clientDao);
        var dbServiceClientWithCache = new DbServiceClientWithCacheImpl(new MyCache<>(new WeakHashMap<>()), clientDao);

        // Warm up
        dbServiceClientSpeed(dbServiceClientWithoutCache, 20);

        Long time_with_cache = dbServiceClientSpeed(dbServiceClientWithCache, 50);
        Long time_without_cache = dbServiceClientSpeed(dbServiceClientWithoutCache, 50);

        logger.info("With cache    time: {}ms", time_with_cache);
        logger.info("Without cache time: {}ms", time_without_cache);
    }

    public static void demoFree() {
        var clientDao = prepareClientDao();
        var dbServiceClientWithWeakCache = new DbServiceClientWithCacheImpl(new MyCache<>(new WeakHashMap<>()), clientDao);
        var dbServiceClientWithStrongCache = new DbServiceClientWithCacheImpl(new MyCache<>(new HashMap<>()), clientDao);

        final var address = new AddressDataSet("S".repeat(1024));
        int BENCH_SIZE = 6 * 1024;

        logger.info("Start dbServiceClientWithWeakCache");
        for (int i = 0; i < BENCH_SIZE; i++) {
            dbServiceClientWithWeakCache.saveClient(new Client(i + "N".repeat(1024), address));
        }

        logger.info("Start dbServiceClientWithStrongCache");
        for (int i = 0; i < BENCH_SIZE; i++) {
            dbServiceClientWithStrongCache.saveClient(new Client(i + "N".repeat(1024), address));
        }
    }

    private static Long dbServiceClientSpeed(DBServiceClient dbServiceClient, int bench_size) {
        long delta = 0, curTime;

        List<Client> clients_ = new ArrayList<>();
        for (int i = 0; i < bench_size; i++) {
            clients_.add(new Client("Name" + i, new AddressDataSet("Street" + i)));
        }

        for (int i = 0; i < bench_size; i++) {
            for (Client client : clients_) {
                curTime = System.currentTimeMillis();
                dbServiceClient.saveClient(client);
                dbServiceClient.getClient(client.getId());
                delta += System.currentTimeMillis() - curTime;
            }
        }
        return delta;
    }

    public static ClientDao prepareClientDao() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        var migrationsExecutorFlyway = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutorFlyway.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        return (ClientDao) new ClientDaoHibernate(sessionManager);
    }
}