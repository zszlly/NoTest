package com.github.zszlly.util;

import com.github.zszlly.exceptions.WrongArgumentsException;
import com.github.zszlly.mark.ProxiedInstance;
import com.github.zszlly.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoTestUtils {

    private static final String LAMBDA_PREFIX = "$$Lambda$";
    public static final VoidArgument VOID_ARGUMENT = new VoidArgument();

    private NoTestUtils() {
    }

    public static boolean isLambda(Class<?> clazz) {
        return clazz.getSimpleName().contains(LAMBDA_PREFIX);
    }

    public static int getInstanceId(Object instance) {
        if (instance == null) {
            return 0;
        }
        if (instance instanceof ProxiedInstance) {
            return ((ProxiedInstance) instance).getInstanceId();
        }
        return instance.hashCode();
    }

    public static List<Argument> toArgumentsList(Object[] arguments) {
        return Arrays.stream(arguments)
                .map(NoTestUtils::toArgument)
                .collect(Collectors.toList());
    }

    public static Argument toArgument(Object instance) {
        if (instance == null) {
            return new NullArgument();
        }
        Class<?> clazz = instance.getClass();
        if (ClassUtils.isPrimitive(clazz)) {
            return new PrimitiveArgument(clazz, instance.toString());
        }
        if (instance instanceof ProxiedInstance) {
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
            Object primitiveValue = ((PrimitiveArgument) wanted).getValueInstance();
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
            if (wanted.getInstanceId() == ((ProxiedInstance) actual).getInstanceId()) {
                return;
            }
            throw new WrongArgumentsException("Invocation want instanceId: " + wanted.getInstanceId() + " but inputted: " + ((ProxiedInstance) actual).getInstanceId());
        }
        throw new WrongArgumentsException("Unsupported mock type: " + wanted.getClass().getName());
    }

}
