package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.AccountDaoImpl;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.dao.ClientDaoImpl;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.jdbc.DataSourceImpl;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceImpl();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        ClientDao clientDao = new ClientDaoImpl(sessionManager, dbExecutor);
        DbExecutorImpl<Account> accountDbExecutor = new DbExecutorImpl<>();
        AccountDao accountDao = new AccountDaoImpl(sessionManager, accountDbExecutor);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client("dbServiceClient", 20));
        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}, age:{}", client.getName(), client.getAge()),
                () -> logger.info("client was not created")
        );
// Работа со счетом
        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var no = dbServiceAccount.saveAccount(new Account("someType", 1.1f));
        Optional<Account> accountOptional = dbServiceAccount.getAccount(no);

        accountOptional.ifPresentOrElse(
                account -> logger.info("created " + account),
                () -> logger.info("account was not created")
        );


    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
