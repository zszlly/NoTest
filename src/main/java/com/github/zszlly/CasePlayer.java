package com.github.zszlly;

import com.github.zszlly.exceptions.WrongReturnValueException;
import com.github.zszlly.mock.NoTestMocker;
import com.github.zszlly.model.Case;
import com.github.zszlly.model.MockedRecord;
import com.github.zszlly.model.Record;
import com.github.zszlly.util.FieldUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CasePlayer implements Runnable {

    private Case c;
    private Map<Integer, NoTestMocker> mockedObjectMap = new HashMap<>();

    private CasePlayer(Case c) {
        this.c = c;
    }

    public static void runCase(Case c) {
        CasePlayer casePlayer = new CasePlayer(c);
        casePlayer.run();
    }

    @Override
    public void run() {
        Method method = c.getRecord().getMethod();
        prepareCase();
        Object returnValue = testMethod(spyInstance(), method, convertArgs());
        mockedObjectMap.values().forEach(NoTestMocker::checkInvocation);
        Object wantedReturnValue = mapInstance(c.getRecord().getReturnValue());
        if (wantedReturnValue == null && returnValue == null) {
            return;
        }
        if (wantedReturnValue == null) {
            throw new WrongReturnValueException("Except: null, but returned: " + returnValue);
        }
        if (!wantedReturnValue.equals(returnValue)) {
            throw new WrongReturnValueException("Except: " + wantedReturnValue + ", but returned: " + returnValue);
        }
    }

    private void prepareCase() {
        c.getMockedInstanceClassTable()
                .forEach((id, clazz) -> {
                    NoTestMocker instanceMocker = NoTestMocker.mock(id, clazz, c.getPrimitiveInstanceTable());
                    mockedObjectMap.put(id, instanceMocker);
                });
        Arrays.stream(c.getActions())
                .forEach(action -> {
                    Record record = action.getRecord();
                    Object[] args = record.getArgs();
                    Object[] mockedArgs = null;
                    if (args != null) {
                        mockedArgs = new Object[args.length];
                        for (int i = 0; i < args.length; i++) {
                            mockedArgs[i] = mapInstance(args[i]);
                        }
                    }
                    mockedObjectMap.get(action.getInstanceId()).addMockedRecord(
                            new MockedRecord(record.getMethod(), mockedArgs, mapInstance(record.getReturnValue())));
                });
    }

    private Object[] convertArgs() {
        Object[] args = c.getRecord().getArgs();
        Object[] mockedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            mockedArgs[i] = mapInstance(args[i]);
        }
        return mockedArgs;
    }

    private Object mapInstance(Object o) {
        if (o instanceof Integer) {
            return mockedObjectMap.get(o).getObject();
        }
        return o;
    }

    private Object spyInstance() {
        try {
            // TODO: enhance this to support create instance without default constructor.
            Class<?> clazz = c.getRecord().getMethod().getDeclaringClass();
            Object instance = clazz.getConstructor().newInstance();
            Map<String, Integer> fieldTable = c.getFieldTable();
            Arrays.stream(clazz.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .forEach(field -> FieldUtils.setField(instance, field, mapInstance(fieldTable.get(field.getName()))));
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
