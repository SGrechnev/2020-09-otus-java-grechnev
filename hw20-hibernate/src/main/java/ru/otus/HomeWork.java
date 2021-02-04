package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        DBServiceClient dbServiceClient = prepareDBServiceClient();

        var clientVasya = new Client("Вася", new AddressDataSet("Lenina"));

        Set<PhoneDataSet> vasyaPhones = new HashSet<>();
        vasyaPhones.add(new PhoneDataSet("007", clientVasya));
        clientVasya.setPhoneSet(vasyaPhones);

        // Сохраняем Васю
        logger.info("===========================================================");
        logger.info("*** SAVE client Вася... {}", clientVasya);
        long id = dbServiceClient.saveClient(clientVasya); // !!! был id = 0, стал id = 1
        logger.info("===========================================================");
        logger.info("*** GET  client Вася...");
        Optional<Client> mayBeCreatedClient = dbServiceClient.getClient(id);
        mayBeCreatedClient.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

        // Вася повзрослел, переехал, сменил телефон
        clientVasya.setName("Василий");
        clientVasya.setAddress(new AddressDataSet("Tomskaya"));
        Set<PhoneDataSet> newVasyaPhones = new HashSet<>();
        newVasyaPhones.add(new PhoneDataSet("007", clientVasya));
        clientVasya.setPhoneSet(newVasyaPhones);

        // Сохраняем Василия
        logger.info("===========================================================");
        logger.info("*** UPDATE client Василий... {}", clientVasya);
        id = dbServiceClient.saveClient(clientVasya);
        logger.info("===========================================================");
        logger.info("*** GET    client Василий...");
        Optional<Client> mayBeUpdatedClient = dbServiceClient.getClient(id);
        mayBeUpdatedClient.ifPresentOrElse((client) -> outputClient("Updated client", client),
                () -> logger.info("Client not found"));
    }

    public static DBServiceClient prepareDBServiceClient() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        var migrationsExecutorFlyway = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutorFlyway.cleanDb();
        migrationsExecutorFlyway.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao userDao = new ClientDaoHibernate(sessionManager);

        return new DbServiceClientImpl(userDao);
    }

    private static void outputClient(String header, Client client) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("client:{}", client);
    }
}
