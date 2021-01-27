package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, Class<T> toCaptureType) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = new EntityClassMetaDataImpl<>(toCaptureType);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
    }

    @Override
    public void insert(T objectData) {
        String query = this.entitySQLMetaData.getInsertSql();
        List<Object> values = getValues(objectData);
        logger.debug("Insert query:  " + query);
        logger.debug("Insert values: " + values);
        try {
            long result = dbExecutor.executeInsert(getConnection(), query, values);
            logger.debug("result executeInsert:" + result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public void update(T objectData) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void insertOrUpdate(T objectData) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public T findById(Object id, Class<T> clazz) {
        String query = this.entitySQLMetaData.getSelectByIdSql();
        this.entityClassMetaData.getIdField();
        logger.info("Select query:  " + query);
        try {
            return dbExecutor.executeSelect(getConnection(), query,
                    id, rs -> processResultSet(rs, clazz)).orElse(null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }
    }

    private T processResultSet(ResultSet rs, Class<T> clazz) {
        try {
            if (rs.next()) {
                Constructor<T> tConstructor = this.entityClassMetaData.getConstructor();
                T obj = tConstructor.newInstance();
                List<Field> fields = this.entityClassMetaData.getAllFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    logger.debug("FieldName: " + field.getName());
                    logger.debug("Value:     " + rs.getObject(field.getName()));
                    field.set(obj, rs.getObject(field.getName()));
                }
                logger.debug("obj: " + obj);
                return obj;
            }
            return null;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error("Reflection error: " + e.getMessage(), e);
        }
        return null;
    }

    private List<Object> getValues(T objectData) {
        List<Field> fields = this.entityClassMetaData.getAllFields();
        return fields.stream().map(field -> {
            field.setAccessible(true);
            try {
                return field.get(objectData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
