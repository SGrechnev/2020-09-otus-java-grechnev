package ru.otus.gson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.json.*;

public class MyGson {

    public String toJson(Object obj) {
        return toJsonValue(obj).toString();
    }

    public JsonValue toJsonValue(Object obj) {
        if (obj == null) {
            return JsonValue.NULL;
        }

        var builder = Json.createObjectBuilder();

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                processField(field, field.get(obj), builder);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return builder.build();
    }

    private void processField(Field field, Object value, JsonObjectBuilder builder) {
        String fieldName = field.getName();
        if (isArray(field)) {
            builder.add(fieldName, Json.createArrayBuilder(getListFromArray(value)));
        } else if (isIntegerNumber(field)) {
            builder.add(fieldName, Long.parseLong(value.toString()));
        } else if (isFloatNumber(field)) {
            builder.add(fieldName, Double.parseDouble(value.toString()));
        } else if (isString(field)) {
            builder.add(fieldName, value.toString());
        } else if (isBoolean(field)) {
            builder.add(fieldName, value.equals(true) ? JsonValue.TRUE : JsonValue.FALSE);
        } else if (isCollection(field)) {
            builder.add(fieldName, Json.createArrayBuilder(getListFromArray(((Collection<Object>) value).toArray())));
        } else {
            builder.add(fieldName, toJsonValue(value));
        }
    }

    private static boolean isIntegerNumber(Field field) {
        return Set.of(Byte.class, Byte.TYPE, Long.class, Long.TYPE, Short.class, Short.TYPE, Integer.class, Integer.TYPE)
                .contains(field.getType());
    }

    private static boolean isFloatNumber(Field field) {
        return Set.of(Double.TYPE, Double.class, Float.TYPE, Float.class).contains(field.getType());
    }

    private static boolean isBoolean(Field field) {
        return (field.getType().equals(Boolean.TYPE));
    }

    private static boolean isString(Field field) {
        return Set.of(String.class, Character.TYPE).contains(field.getType());
    }

    private static boolean isCollection(Field field) {
        return field.getType().equals(Collection.class);
    }

    private static boolean isArray(Field field) {
        return (field.getType().isArray());
    }

    private static List<Object> getListFromArray(Object array) {
        if (array.getClass().isArray()) {
            List<Object> objectList = new ArrayList<>();
            for (int i = 0; i < Array.getLength(array); i++) {
                objectList.add(Array.get(array, i));
            }
            return objectList;
        }
        return null;
    }
}