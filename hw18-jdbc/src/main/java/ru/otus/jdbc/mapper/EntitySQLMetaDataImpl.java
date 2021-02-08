package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(Class<T> toCaptureType) {
        this.entityClassMetaData = new EntityClassMetaDataImpl<>(toCaptureType);
    }

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        String id = entityClassMetaData.getIdField().getName();
        return this.getSelectAllSql() + " WHERE " + id + "=?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = entityClassMetaData.getAllFields();
        String columnNames = fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        var query = new StringBuilder("INSERT INTO ");
        query.append(entityClassMetaData.getName())
                .append("(")
                .append(columnNames)
                .append(") VALUES (?")
                .append(", ?".repeat(fields.size() - 1))
                .append(")");
        return query.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }
}
