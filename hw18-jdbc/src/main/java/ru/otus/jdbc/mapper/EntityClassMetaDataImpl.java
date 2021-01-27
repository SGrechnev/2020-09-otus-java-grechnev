package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger logger = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);
    Class<T> capturedType;

    public EntityClassMetaDataImpl(Class<T> toCaptureType) {
        this.capturedType = toCaptureType;
        logger.debug("Captured type: " + this.capturedType);
    }

    @Override
    public String getName() {
        return capturedType.getSimpleName().toLowerCase();
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
        List<Field> fields = this.getAllFields();
        var idFields = fields.stream().filter(field -> field.isAnnotationPresent(Id.class));
        return idFields.findFirst().get();
    }

    @Override
    public List<Field> getAllFields() {
        Field[] fields = capturedType.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(EntityClassMetaDataImpl::serializable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        var fields = getAllFields();
        var fieldId = getIdField();
        fields.remove(fieldId);
        return fields;
    }

    private static boolean serializable(Field field) {
        return !Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers());
    }
}
