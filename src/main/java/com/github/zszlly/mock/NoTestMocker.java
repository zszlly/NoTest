package com.github.zszlly.mock;

import com.github.zszlly.model.MockedRecord;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoTestMocker implements MethodInterceptor {

    private final int instanceId;
    private boolean isPrimitive = false;
    private Map<Method, MethodMocker> methodMockerMap = new HashMap<>();
    private Object object;

    private NoTestMocker(int instanceId) {
        this.instanceId = instanceId;
    }

    public static NoTestMocker mock(Integer id, Class<?> tClass, Map<Integer, String> primitiveInstanceTable) {
        NoTestMocker mocker = new NoTestMocker(id);
        Object instance = null;
        if (tClass.isPrimitive()) {
            mocker.setPrimitive(true);
            String value = primitiveInstanceTable.get(id);
            if (tClass == boolean.class || tClass == Boolean.class) {
                instance = Boolean.valueOf(value);
            }
            if (tClass == char.class || tClass == Character.class) {
                if (value.length() > 1) {
                    throw new IllegalArgumentException("Wrong value, want a character but get [" + value + "].");
                }
                instance = value.charAt(0);
            }
            if (tClass == byte.class || tClass == Byte.class) {
                instance = Byte.valueOf(value);
            }
            if (tClass == short.class || tClass == Short.class) {
                instance = Short.valueOf(value);
            }
            if (tClass == int.class || tClass == Integer.class) {
                instance = Integer.valueOf(value);
            }
            if (tClass == long.class || tClass == Long.class) {
                instance = Long.valueOf(value);
            }
            if (tClass == float.class || tClass == Float.class) {
                instance = Float.valueOf(value);
            }
            if (tClass == double.class || tClass == Double.class) {
                instance = Double.valueOf(value);
            }
            // do nothing if its type is void/Void.
        } else if (String.class == tClass) {
            mocker.setPrimitive(true);
            instance = primitiveInstanceTable.get(id);
        } else {
            Enhancer e = new Enhancer();
            if (tClass.isInterface()) {
                e.setInterfaces(new Class[]{tClass});
            } else {
                e.setSuperclass(tClass);
            }
            e.setCallback(mocker);
            instance = e.create();
        }
        mocker.setObject(instance);
        return mocker;
    }

    public void addMockedRecord(MockedRecord mockedRecord) {
        if (isPrimitive) {
            throw new IllegalArgumentException("Cannot mock method in primitive instance, instanceId=" + instanceId);
        }
        methodMockerMap.computeIfAbsent(mockedRecord.getMethod(), MethodMocker::new).addMockedRecord(mockedRecord);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        return methodMockerMap.get(method).invoke(args);
    }

    public void checkInvocation() {
        methodMockerMap.values().forEach(MethodMocker::checkInvocationTimes);
    }

    public int getInstanceId() {
        return instanceId;
    }

    public Object getObject() {
        return object;
    }

    private void setObject(Object object) {
        this.object = object;
    }

    private void setPrimitive(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }
}
