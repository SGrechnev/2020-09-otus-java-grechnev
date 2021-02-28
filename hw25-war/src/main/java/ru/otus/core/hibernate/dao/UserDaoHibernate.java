package ru.otus.core.hibernate.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.core.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

//@Repository
public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, login));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        return currentSession.getHibernateSession().createQuery("SELECT m FROM User m", User.class).getResultList();
    }

    @Override
    public String insert(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getLogin();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public String insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (!user.getLogin().equals("")) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getLogin();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
