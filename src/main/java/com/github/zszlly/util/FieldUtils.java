package com.github.zszlly.util;

import java.lang.reflect.Field;

public class FieldUtils {

    private FieldUtils() {

    }

    public static void setField(Object obj, String fieldName, Object fieldValue) {
        try {
            setField(obj, obj.getClass().getDeclaredField(fieldName), fieldValue) ;
        } catch (NoSuchFieldException e) {
            SneakyThrow.sneakyThrow(e);
        }
    }

    public static void setField(Object obj, Field field, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            SneakyThrow.sneakyThrow(e);
        }
    }

}
