package com.jschapin.dregs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MappedRecord {

    static <T extends Record> T fromSystem(Class<T> clazz) {
        return fromMap(System.getenv(), clazz, "");
    }

    static <T extends Record> T fromSystem(Class<T> clazz, String keyPrefix) {
        return fromMap(System.getenv(), clazz, keyPrefix);
    }

    static <T extends Record> T fromMap(Map<String, String> map, Class<T> clazz) {
        return fromMap(map, clazz, "");
    }

    static <T extends Record> T fromMap(Map<String, String> map, Class<T> clazz, String keyPrefix) {
        return clazz.cast(process(map, clazz, keyPrefix));
    }

    private static Record process(Map<String, String> map, Class<? extends Record> clazz, String keyPrefix) {
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException("Provided class is not a Record");
        }

        String prefix = (keyPrefix.isBlank() ? "" : keyPrefix.toUpperCase() + "_");
        List<Class<?>> types = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (RecordComponent component : clazz.getRecordComponents()) {
            var type = component.getType();
            types.add(type);
            var key = prefix + componentNameToKey(component.getName());
            if (type.isRecord()) {
                //noinspection unchecked
                var subRecord = process(map, (Class<? extends Record>) type, key);
                values.add(subRecord);
            } else {
                var rawValue = map.get(key);
                var parsedValue = parseValue(type, rawValue);
                values.add(parsedValue);
            }
        }

        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(types.toArray(new Class<?>[1]));
            return (Record) declaredConstructor.newInstance(values.toArray());
        } catch (NoSuchMethodException |
                 IllegalAccessException |
                 java.lang.InstantiationException |
                 InvocationTargetException
                e
        ) {
            throw new InstantiationException(e);
        }
    }

    private static String componentNameToKey(String fieldName) {
        return fieldName
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase();
    }

    private static Object parseValue(Type type, String rawValue) {
        if (rawValue == null) {
            return null;
        }
        return switch (type) {
            case Class<?> cl when cl == String.class -> rawValue;
            case Class<?> cl when cl == Character.class || cl == char.class ->
                    rawValue.charAt(0); // Throws IndexOutOfBounds is empty
            case Class<?> cl when cl == Byte.class || cl == byte.class -> Byte.parseByte(rawValue);
            case Class<?> cl when cl == Short.class || cl == short.class -> Short.parseShort(rawValue);
            case Class<?> cl when cl == Integer.class || cl == int.class -> Integer.parseInt(rawValue);
            case Class<?> cl when cl == Long.class || cl == long.class -> Long.parseLong(rawValue);
            case Class<?> cl when cl == Float.class || cl == float.class -> Float.parseFloat(rawValue);
            case Class<?> cl when cl == Double.class || cl == double.class -> Double.parseDouble(rawValue);
            case Class<?> cl when cl == Boolean.class || cl == boolean.class -> Boolean.parseBoolean(rawValue);
            default -> throw new UnsupportedComponentException("Record components must be strings or primitive values");
        };
    }

}
