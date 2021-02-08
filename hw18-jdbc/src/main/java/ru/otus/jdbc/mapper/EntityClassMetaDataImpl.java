package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger logger = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);
    private final Class<T> capturedType;
    private final String name;
    private final List<Field> fields;
    private final Field idField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> toCaptureType) {
        this.capturedType = toCaptureType;
        logger.debug("Captured type: {}", this.capturedType);

        this.name = capturedType.getSimpleName().toLowerCase();
        logger.trace("Table name: {}", this.name);

        Field[] fields = capturedType.getDeclaredFields();
        this.fields = Arrays.stream(fields)
                .filter(EntityClassMetaDataImpl::serializable)
                .collect(Collectors.toList());

        var idList = this.fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).collect(Collectors.toList());
        if(idList.size() != 1){
            String sIdList = idList.stream() // id field names
                    .map(Field::getName)
                    .collect(Collectors.joining(", "));
            throw new JdbcException(String.format("%s has %d id fields, expected 1. Id fields: [%s]",
                    this.capturedType.getName(),
                    idList.size(),
                    sIdList)
            );
        }
        this.idField = idList.get(0);

        this.fieldsWithoutId = new ArrayList<>(this.fields);
        this.fieldsWithoutId.remove(this.idField);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return capturedType.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new JdbcException(e);
        }
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return new ArrayList<>(this.fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return new ArrayList<>(this.fieldsWithoutId);
    }

    private static boolean serializable(Field field) {
        return !Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers());
    }
}
