package com.github.zszlly.player.mock;

import com.github.zszlly.util.NoTestUtils;
import com.github.zszlly.exceptions.TooLessInvocationException;
import com.github.zszlly.exceptions.TooManyInvocationException;
import com.github.zszlly.exceptions.WrongArgumentsException;
import com.github.zszlly.model.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Mocker and validation for each method will be invoked by the tested method.
 */
public class MethodMocker {

    private final Method method;
    private final Map<Integer, InstanceMocker> mockedObjectMap;
    private List<Record> recordList = new LinkedList<>();
    private int step = 0;

    public MethodMocker(Method method, Map<Integer, InstanceMocker> mockedObjectMap) {
        this.method = method;
        this.mockedObjectMap = mockedObjectMap;
    }

    public void addRecord(Record mockedRecord) {
        recordList.add(mockedRecord);
    }

    // invoke and valid each invocation
    public Object invoke(Object[] args) {
        Record[] records = recordList.toArray(new Record[0]);
        if (step == records.length) {
            throw new TooManyInvocationException("Method [" + method.getName() + "] want invoke [" + records.length + "] times but too many invocation.");
        }
        Record record = records[step++];
        Argument[] wantedArgs = record.getArgs();
        args = args != null && args.length == 0 ? null : args;
        wantedArgs = wantedArgs != null && wantedArgs.length == 0 ? null : wantedArgs;
        if (wantedArgs == null && args == null) {
            return toInstance(record.getReturnValue());
        }
        if (wantedArgs == null) {
            throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want no arguments but inputted " + Arrays.toString(args));
        }
        if (args == null) {
            throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want " + Arrays.toString(wantedArgs) + " but no argument inputted.");
        }
        if (wantedArgs.length != args.length) {
            throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want " + Arrays.toString(wantedArgs) + " but inputted " + Arrays.toString(args));
        }
        try {
            for (int i = 0; i < wantedArgs.length; i++) {
                NoTestUtils.validInstance(wantedArgs[i], args[i]);
            }
            return toInstance(record.getReturnValue());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while invoking method: " + method.getName(), e);
        }
    }

    public void checkInvocationTimes() {
        if (step != recordList.size()) {
            throw new TooLessInvocationException("Method [" + method.getName() + "] want invoke [" + recordList.size() + "] times but invoked [" + step + "] times.");
        }
    }

    private Object toInstance(Argument argument) {
        if (argument == null) {
            return null;
        }
        if (argument instanceof PrimitiveArgument) {
            return ((PrimitiveArgument) argument).getValue();
        }
        if (argument instanceof MockedArgument) {
            return mockedObjectMap.get(argument.getInstanceId()).getInstance();
        }
        throw new IllegalArgumentException("Unsupported converting of argument: " + argument.toString());
    }

    public Method getMethod() {
        return method;
    }
}
