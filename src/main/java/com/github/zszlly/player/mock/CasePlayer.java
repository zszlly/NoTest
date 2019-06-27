package com.github.zszlly.player.mock;

import com.github.zszlly.model.MockedArgument;
import com.github.zszlly.model.PrimitiveArgument;
import com.github.zszlly.util.NoTestUtils;
import com.github.zszlly.model.Argument;
import com.github.zszlly.model.Case;
import com.github.zszlly.util.FieldUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * NoTest case playback entry, read case and mock instances then run the test.
 */
public class CasePlayer implements Runnable {

    private Case c;
    private Map<Integer, InstanceMocker> mockerMap = new HashMap<>();

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
        mockerMap.put(0, new InstanceMocker(0, mockerMap));
        prepareCase();
        Object returnValue = testMethod(proxyInstance(), method, convertArgs());
        mockerMap.values().forEach(InstanceMocker::checkInvocation);
        NoTestUtils.validInstance(c.getRecord().getReturnValue(), returnValue);
    }

    private void prepareCase() {
        c.getMockedInstanceClassTable()
                .forEach((id, clazz) -> {
                    InstanceMocker instanceMocker = InstanceMocker.mock(id, clazz, mockerMap);
                    mockerMap.put(id, instanceMocker);
                });
        Arrays.stream(c.getActions())
                .forEach(action ->
                    mockerMap.get(action.getInstanceId()).addRecord(action.getRecord()));
    }

    private Object[] convertArgs() {
        Argument[] args = c.getRecord().getArgs();
        Object[] mockedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            mockedArgs[i] = mapInstance(args[i]);
        }
        return mockedArgs;
    }

    private Object mapInstance(Argument arg) {
        if (arg instanceof PrimitiveArgument) {
            return ((PrimitiveArgument) arg).getValue();
        }
        if (arg instanceof MockedArgument) {
            return mockerMap.get(arg.getInstanceId()).getInstance();
        }
        throw new IllegalArgumentException("Unsupported argument: " + arg.toString());
    }

    private Object proxyInstance() {
        try {
            // TODO: enhance this to support create instance without default constructor.
            Class<?> clazz = c.getRecord().getMethod().getDeclaringClass();
            Object instance = clazz.getConstructor().newInstance();
            Map<String, Argument> fieldTable = c.getFieldTable();
            Arrays.stream(clazz.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .forEach(field -> FieldUtils.setValue(instance, field, mapInstance(fieldTable.get(field.getName()))));
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
