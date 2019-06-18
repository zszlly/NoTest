package com.github.zszlly.util;

import java.lang.reflect.Field;

public class FieldUtils {

    private FieldUtils() {

    }

    public static <T> void setField(T t, String fieldName, Object fieldValue) {
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(t, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
