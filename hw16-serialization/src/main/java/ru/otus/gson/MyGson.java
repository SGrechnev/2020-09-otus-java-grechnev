package ru.otus.gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.json.*;

public class MyGson {
    private static final Logger logger = LoggerFactory.getLogger(MyGson.class);

    private static final int MAX_RECURSION_DEPTH = 4;

    public String toJson(Object obj) {
        if (obj != null) {
            logger.debug("classname: " + obj.getClass().getName());
        }
        return toJsonValue(obj, 0).toString();
    }

    private JsonValue toJsonValue(Object obj, int recursionDepth) {
        if (recursionDepth > MAX_RECURSION_DEPTH) {
            throw new RecursionException("Recursion depth is too large: " + recursionDepth);
        }
        String indent = "  ".repeat(recursionDepth);
        if (obj == null) {
            logger.debug(indent + "process null");
            return JsonValue.NULL;
        } else if (obj.getClass().isArray()) {
            logger.debug(indent + "process array");
            return processArray(obj, recursionDepth);
        } else if (obj instanceof java.util.Collection) {
            logger.debug(indent + "process Collection");
            return processArray(((Collection<?>) obj).toArray(), recursionDepth);
        } else if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long) {
            logger.debug(indent + "process integer number");
            return Json.createValue(Long.parseLong(obj.toString()));
        } else if (obj instanceof Boolean) {
            logger.debug(indent + "process boolean");
            return obj.equals(true) ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (obj instanceof Float || obj instanceof Double) {
            logger.debug(indent + "process float or double");
            return Json.createValue(Double.parseDouble(obj.toString()));
        } else if (obj instanceof String || obj instanceof Character) {
            logger.debug(indent + "process string");
            return Json.createValue(obj.toString());
        }

        logger.debug(indent + "process object");
        var fields = obj.getClass().getDeclaredFields();
        logger.trace(indent + "fields: " + Arrays.toString(fields));

        var subBuilder = Json.createObjectBuilder();
        for (Field field : fields) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                logger.debug(indent + " process field " + field.getType().getSimpleName() + " " + field.getName());
                subBuilder.add(field.getName(), toJsonValue(field.get(obj), recursionDepth + 1));
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return subBuilder.build();
    }

    private JsonArray processArray(Object array, int recursionDepth) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Expected array, consumed " + array.getClass().getName());
        }

        var arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < Array.getLength(array); i++) {
            arrayBuilder.add(toJsonValue(Array.get(array, i), recursionDepth + 1));
        }

        return arrayBuilder.build();
    }
}