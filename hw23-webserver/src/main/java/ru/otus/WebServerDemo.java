package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.cachehw.MyCache;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUser;
import ru.otus.core.service.DbServiceUserWithCacheImpl;
import ru.otus.flyway.MigrationsExecutorFlyway;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

import java.util.WeakHashMap;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user?login=admin
*/
public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var dbServiceUser = prepareDbServiceUser();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    public static DbServiceUser prepareDbServiceUser() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        var migrationsExecutorFlyway = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutorFlyway.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        var userDao = new UserDaoHibernate(sessionManager);

        return new DbServiceUserWithCacheImpl(new MyCache<>(new WeakHashMap<>()), userDao);
    }
}
