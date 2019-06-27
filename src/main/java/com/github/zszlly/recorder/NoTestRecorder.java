package com.github.zszlly.recorder;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.io.CaseSaver;
import com.github.zszlly.mark.SpiedInstance;
import com.github.zszlly.model.Argument;
import com.github.zszlly.model.Record;
import com.github.zszlly.util.FieldUtils;
import com.github.zszlly.util.NoTestUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NoTestRecorder implements MethodInterceptor {

    private final Object originalInstance;
    private final CaseSaver saver;
    private Object spiedInstance;

    private NoTestRecorder(Object originalInstance, CaseSaver saver) {
        this.originalInstance = originalInstance;
        this.saver = saver;
    }

    @SuppressWarnings("unchecked")
    public static <T> T record(T instance, CaseSaver saver) {
        Class<?> clazz = instance.getClass();
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(NoTestRecorder::setPermission);
        Enhancer e = new Enhancer();
        e.setSuperclass(clazz);
        NoTestRecorder recorder = new NoTestRecorder(instance, saver);
        e.setCallback(recorder);
        recorder.spiedInstance = e.create();
        return (T) recorder.spiedInstance;
    }

    private static void setPermission(Method method) {
        method.setAccessible(true);
    }

    static Object proxyInstance(Object instance, CaseBuilder caseBuilder) {
        if (instance == null) {
            return null;
        }
        if (instance instanceof SpiedInstance || NoTestUtils.isPrimitive(instance)) {
            return instance;
        }
        Map<Integer, Class<?>> mockedInstanceClassTable = caseBuilder.getMockedInstanceClassTable();
        Class<?> clazz = instance.getClass();
        Enhancer e = new Enhancer();
        if (NoTestUtils.isLambda(clazz)) {
            Class<?> interfaceClass = clazz.getInterfaces()[0];
            mockedInstanceClassTable.put(instance.hashCode(), interfaceClass);
            e.setInterfaces(new Class[]{interfaceClass, SpiedInstance.class});
        } else {
            mockedInstanceClassTable.put(instance.hashCode(), clazz);
            e.setSuperclass(clazz);
            e.setInterfaces(new Class[]{SpiedInstance.class});
        }
        e.setCallback(new NoTestProxy(instance, caseBuilder));
        return e.create();
    }

    // method entry
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!method.isAnnotationPresent(NoTest.class)) {
            return method.invoke(originalInstance, args);
        }
        // proxy arguments
        CaseBuilder caseBuilder = new CaseBuilder();
        Map<String, Argument> fieldTable = caseBuilder.getFieldTable();
        Object[] spiedArgs = Arrays.stream(args)
                .map(arg -> proxyInstance(arg, caseBuilder))
                .toArray();
        // proxy fields
        Map<Field, Object> originalFieldValues = new HashMap<>();
        Arrays.stream(originalInstance.getClass().getDeclaredFields())
                .peek(FieldUtils::setAccessible)
                .forEach(field -> {
                            Object value = FieldUtils.getValue(field, spiedInstance);
                            if (!NoTestUtils.isPrimitive(value)) {
                                originalFieldValues.put(field, value);
                            }
                            FieldUtils.setValue(originalInstance, field, value);
                            fieldTable.put(field.getName(), NoTestUtils.toArgument(value));
                        }
                );

        // invoke
        Object returnValue = method.invoke(originalInstance, spiedArgs);
        Argument[] arguments = Arrays.stream(spiedArgs)
                .map(NoTestUtils::toArgument)
                .toArray(Argument[]::new);
        Record record = new Record(method, arguments, NoTestUtils.toArgument(returnValue));
        caseBuilder.setRecord(record);
        saver.addCase(caseBuilder.build());
        // restore fields
        originalFieldValues.forEach((field, value) -> FieldUtils.setValue(originalInstance, field, value));
        return returnValue;
    }

    public void setSpiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
    }

}
