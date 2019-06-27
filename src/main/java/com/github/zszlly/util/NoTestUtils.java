package com.github.zszlly.util;

import com.github.zszlly.exceptions.WrongArgumentsException;
import com.github.zszlly.mark.SpiedInstance;
import com.github.zszlly.model.*;

import java.util.*;

public class NoTestUtils {

    private static final String LAMBDA_PRIFIX = "$$Lambda$";
    private static final Set<Class<?>> PRIMITIVE_CLASSES = new HashSet<>();

    static {
        PRIMITIVE_CLASSES.add(boolean.class);
        PRIMITIVE_CLASSES.add(char.class);
        PRIMITIVE_CLASSES.add(byte.class);
        PRIMITIVE_CLASSES.add(short.class);
        PRIMITIVE_CLASSES.add(int.class);
        PRIMITIVE_CLASSES.add(long.class);
        PRIMITIVE_CLASSES.add(float.class);
        PRIMITIVE_CLASSES.add(double.class);
        PRIMITIVE_CLASSES.add(void.class);
        PRIMITIVE_CLASSES.add(Boolean.class);
        PRIMITIVE_CLASSES.add(Character.class);
        PRIMITIVE_CLASSES.add(Byte.class);
        PRIMITIVE_CLASSES.add(Short.class);
        PRIMITIVE_CLASSES.add(Integer.class);
        PRIMITIVE_CLASSES.add(Long.class);
        PRIMITIVE_CLASSES.add(Float.class);
        PRIMITIVE_CLASSES.add(Double.class);
        PRIMITIVE_CLASSES.add(Void.class);
        PRIMITIVE_CLASSES.add(String.class);
    }

    private NoTestUtils() {
    }

    public static boolean isPrimitive(Object instance) {
        return isPrimitive(instance.getClass());
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return PRIMITIVE_CLASSES.contains(clazz);
    }

    public static boolean isLambda(Class<?> clazz) {
        return clazz.getSimpleName().contains(LAMBDA_PRIFIX);
    }

    public static int getInstanceId(Object instance) {
        if (instance == null) {
            return 0;
        }
        if (instance instanceof SpiedInstance) {
            return ((SpiedInstance) instance).getInstanceId();
        }
        return instance.hashCode();
    }

    public static Argument toArgument(Object instance) {
        Class<?> clazz = instance.getClass();
        if (isPrimitive(clazz)) {
            return new PrimitiveArgument(clazz, instance.toString());
        }
        if (instance instanceof SpiedInstance) {
            return new MockedArgument(getInstanceId(instance));
        }
        return new GeneratedByMethodArgument(clazz);
    }

    public static void validInstance(Argument wanted, Object actual) {
        if (wanted.getInstanceId() == 0) {
            // want null as argument
            if (actual == null) {
                return;
            }
            throw new WrongArgumentsException("Invocation want NULL but inputted: " + actual.getClass());
        }
        if (wanted instanceof PrimitiveArgument) {
            Object primitiveValue = ((PrimitiveArgument) wanted).getValue();
            if (primitiveValue.equals(actual)) {
                return;
            }
            throw new WrongArgumentsException("Invocation want primitive value: " + primitiveValue + " but inputted: " + actual);
        }
        if (wanted instanceof GeneratedByMethodArgument) {
            // this argument is created by tested method, no need to valid.
            GeneratedByMethodArgument generatedByMethodArgument = (GeneratedByMethodArgument) wanted;
            if (generatedByMethodArgument.getObjectClass() == actual.getClass()) {
                return;
            }
            throw new WrongArgumentsException("Invocation want class: " + generatedByMethodArgument.getObjectClass() + " but inputted: " + actual.getClass());
        }
        if (wanted instanceof MockedArgument) {
            if (wanted.getInstanceId() == ((SpiedInstance) actual).getInstanceId()) {
                return;
            }
            throw new WrongArgumentsException("Invocation want instanceId: " + wanted.getInstanceId() + " but inputted: " + ((SpiedInstance) actual).getInstanceId());
        }
        throw new WrongArgumentsException("Unsupported mock type: " + wanted.getClass().getName());
    }

}
