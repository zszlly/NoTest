package com.github.zszlly;

import com.github.zszlly.exceptions.WrongReturnValueException;
import com.github.zszlly.mock.MockedClassMark;
import com.github.zszlly.mock.NoTestMocker;
import com.github.zszlly.model.Case;
import com.github.zszlly.model.MockedRecord;
import com.github.zszlly.model.Record;
import com.github.zszlly.util.SneakyThrow;

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
                            Method cMethod = c.getRecord().getMethod();
                            return cMethod.equals(method) && Arrays.equals(cMethod.getParameterTypes(), method.getParameterTypes());
                        })
                        .forEach(c -> {
                            Map<Integer, NoTestMocker> mockedObjectMap = prepareCase(c);
                            Object returnValue = testMethod(spyInstance(c, mockedObjectMap), method, convertArgs(c, mockedObjectMap));
                            mockedObjectMap.values().forEach(NoTestMocker::checkInvocation);
                            Object wantedReturnValue = mockedObjectMap.get((c.getRecord().getReturnValue()).getInstanceId()).getObject();
                            if (wantedReturnValue == null && returnValue == null) {
                                return;
                            }
                            if (wantedReturnValue == null) {
                                throw new WrongReturnValueException("Except: null, but returned: " + returnValue);
                            }
                            if (!wantedReturnValue.equals(returnValue)) {
                                throw new WrongReturnValueException("Except: " + wantedReturnValue + ", but returned: " + returnValue);
                            }
                        })
        );
    }

    private Map<Integer, NoTestMocker> prepareCase(Case c) {
        Map<Integer, NoTestMocker> mockedObjectMap = new HashMap<>();
        c.getMockedInstanceClassTable()
                .forEach((id, klazz) -> {
                    NoTestMocker instanceMocker = NoTestMocker.mock(id, klazz, c.getPrimitiveInstanceTable());
                    mockedObjectMap.put(id, instanceMocker);
                });
        Arrays.stream(c.getActions())
                .forEach(action -> {
                    Record record = action.getRecord();
                    MockedClassMark[] args = record.getArgs();
                    Object[] mockedArgs = null;
                    if (args != null) {
                        mockedArgs = new Object[args.length];
                        for (int i = 0; i < args.length; i++) {
                            mockedArgs[i] = mockedObjectMap.get(args[i].getInstanceId()).getObject();
                        }
                    }
                    mockedObjectMap.get(action.getInstanceId()).addMockedRecord(
                            new MockedRecord(record.getMethod(), mockedArgs, mockedObjectMap.get(record.getReturnValue().getInstanceId()).getObject()));
                });
        return mockedObjectMap;
    }

    private Object[] convertArgs(Case c, Map<Integer, NoTestMocker> mockedObjectMap) {
        MockedClassMark[] args = c.getRecord().getArgs();
        Object[] mockedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            mockedArgs[i] = mockedObjectMap.get((args[i]).getInstanceId()).getObject();
        }
        return mockedArgs;
    }

    private Object spyInstance(Case c, Map<Integer, NoTestMocker> mockedObjectMap) {
        try {
            // TODO: enhance this to support create instance without default constructor.
            Object instance = clazz.getConstructor().newInstance();
            Map<String, Integer> fieldTable = c.getFieldTable();
            Arrays.stream(clazz.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .forEach(field -> {
                        try {
                            field.set(instance, mockedObjectMap.get(fieldTable.get(field.getName())).getObject());
                        } catch (IllegalAccessException e) {
                            SneakyThrow.sneakyThrow(e);
                        }
                    });
            return instance;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object testMethod(Object instance, Method method, Object[] args) {
        try {
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

}
