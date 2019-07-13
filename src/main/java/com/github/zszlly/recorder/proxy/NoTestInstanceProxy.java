package com.github.zszlly.recorder.proxy;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.io.CaseSaver;
import com.github.zszlly.mark.ProxiedInstance;
import com.github.zszlly.model.Argument;
import com.github.zszlly.model.Record;
import com.github.zszlly.util.ClassUtils;
import com.github.zszlly.util.FieldUtils;
import com.github.zszlly.util.NoTestUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An instance proxy, will record this invocation and save it in Case.
 */
public class NoTestInstanceProxy implements MethodInterceptor {

    private final Object originalInstance;
    private final CaseSaver saver;
    private Object proxiedInstance;

    public NoTestInstanceProxy(Object originalInstance, CaseSaver saver) {
        this.originalInstance = originalInstance;
        this.saver = saver;
    }

    public static void setPermission(Method method) {
        method.setAccessible(true);
    }

    static Object proxyInstance(Object instance, CaseBuilder caseBuilder) {
        if (instance == null) {
            return null;
        }
        if (instance instanceof ProxiedInstance || ClassUtils.isPrimitive(instance)) {
            return instance;
        }
        Map<Integer, Class<?>> mockedInstanceClassTable = caseBuilder.getMockedInstanceClassTable();
        Class<?> clazz = instance.getClass();
        Enhancer e = new Enhancer();
        if (NoTestUtils.isLambda(clazz)) {
            Class<?> interfaceClass = clazz.getInterfaces()[0];
            mockedInstanceClassTable.put(instance.hashCode(), interfaceClass);
            e.setInterfaces(new Class[]{interfaceClass, ProxiedInstance.class});
        } else {
            mockedInstanceClassTable.put(instance.hashCode(), clazz);
            e.setSuperclass(clazz);
            e.setInterfaces(new Class[]{ProxiedInstance.class});
        }
        e.setCallback(new NoTestActionRecorder(instance, caseBuilder));
        return e.create();
    }

    private static Object getOriginalObject(Object instance) {
        if (instance == null || ClassUtils.isPrimitive(instance)) {
            return instance;
        }
        if (!(instance instanceof ProxiedInstance)) {
            throw new IllegalArgumentException("Trying to get original instance from a non-proxied instance: " + instance);
        }
        return ((ProxiedInstance) instance).getNoTestInstanceProxyOriginalInstance();
    }

    private static void replaceFieldValue(Object instance, Field[] fields, Object[] values) {
        for (int i = 0; i < fields.length; i++) {
            FieldUtils.setValue(instance, fields[i], values[i]);
        }
    }

    // method entry
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Field[] fields = originalInstance.getClass().getDeclaredFields();
        if (!method.isAnnotationPresent(NoTest.class)) {
            Object returnValue = method.invoke(originalInstance, args);
            // update proxied fields
            replaceFieldValue(proxiedInstance, fields, Arrays.stream(fields)
                    .map(field -> FieldUtils.getValue(field, originalInstance))
                    .toArray());
            return returnValue;
        }
        // proxy arguments
        CaseBuilder caseBuilder = new CaseBuilder();
        Map<String, Argument> fieldTable = caseBuilder.getFieldTable();
        Object[] proxiedArgs = Arrays.stream(args)
                .map(arg -> proxyInstance(arg, caseBuilder))
                .toArray();
        // proxy fields
        Object[] proxiedFieldValues = Arrays.stream(fields)
                .map(field -> proxyInstance(FieldUtils.getValue(field, proxiedInstance), caseBuilder))
                .toArray();
        replaceFieldValue(originalInstance, fields, proxiedFieldValues);
        for (int i = 0; i < fields.length; i++) {
            fieldTable.put(fields[i].getName(), NoTestUtils.toArgument(proxiedFieldValues[i]));
        }
        // invoke
        Object returnValue = method.invoke(originalInstance, proxiedArgs);
        List<Argument> arguments = Arrays.stream(proxiedArgs)
                .map(NoTestUtils::toArgument)
                .collect(Collectors.toList());
        Record record = new Record(method, arguments, NoTestUtils.toArgument(returnValue));
        caseBuilder.setRecord(record);
        saver.addCase(caseBuilder.build());
        // revert fields
        replaceFieldValue(proxiedInstance, fields, Arrays.stream(proxiedFieldValues)
                .map(NoTestInstanceProxy::getOriginalObject)
                .toArray());
        return returnValue;
    }

    public void setProxiedInstance(Object proxiedInstance) {
        this.proxiedInstance = proxiedInstance;
    }
}
