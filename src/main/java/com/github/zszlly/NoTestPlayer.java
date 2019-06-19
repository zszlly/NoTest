package com.github.zszlly;

import com.github.zszlly.exceptions.WrongReturnValueException;
import com.github.zszlly.mock.MockedClassMark;
import com.github.zszlly.mock.NoTestMocker;
import com.github.zszlly.model.Case;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NoTestPlayer {

    private Collection<Case> cases;
    private Class<?> clazz;

    public NoTestPlayer(Collection<Case> cases, Class<?> clazz) {
        this.cases = cases;
        this.clazz = clazz;
    }

    public void play() {
        MethodScanner.scanMethods(clazz).forEach(method ->
                cases.stream()
                        .filter(c -> {
                            Method cMethod = c.getMethod();
                            return cMethod.equals(method) && Arrays.equals(cMethod.getParameterTypes(), method.getParameterTypes());
                        })
                        .forEach(c -> {
                            Object returnValue = testMethod(prepareCase(c), method, c.getArgs());
                            if (c.getReturnValue() == null && returnValue == null) {
                                return;
                            }
                            if (c.getReturnValue() == null && returnValue != null) {
                                throw new WrongReturnValueException("Except: null, but returned: " + returnValue);
                            }
                            if (!c.getReturnValue().equals(returnValue)) {
                                throw new WrongReturnValueException("Except: " + c.getReturnValue() + ", but returned: " + returnValue);
                            }
                        })
        );
    }

    private Map<Integer, NoTestMocker> prepareCase(Case c) {
        Map<Integer, NoTestMocker> mockedObjectMap = new HashMap<>();
        c.getMockedInstanceClassTable()
                .forEach((id, klazz) -> NoTestMocker.mock(id, klazz, mockedObjectMap));
        Arrays.stream(c.getRecords())
                .forEach(record -> mockedObjectMap.get(record.getObjectId()).addInvocationRecord(record));
        Object[] args = c.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MockedClassMark) {
                args[i] = mockedObjectMap.get(((MockedClassMark) args[i]).getInstanceId()).getObject();
            }
        }
        return mockedObjectMap;
    }

    private Object testMethod(Map<Integer, NoTestMocker> mockedObjectMap, Method method, Object[] args) {
        try {
            method.setAccessible(true);
            Object returnValue = method.invoke(clazz.getConstructor().newInstance(), args);
            mockedObjectMap.values().forEach(NoTestMocker::checkInvocation);
            return returnValue;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

}
