package com.github.zszlly.mock;

import com.github.zszlly.exceptions.TooLessInvocationException;
import com.github.zszlly.exceptions.TooManyInvocationException;
import com.github.zszlly.exceptions.WrongArgumentsException;
import com.github.zszlly.model.MockedRecord;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MethodMocker {

    private Method method;
    private List<MockedRecord> recordLinkedList = new LinkedList<>();
    private int step = 0;

    public MethodMocker(Method method) {
        this.method = method;
    }

    public void addMockedRecord(MockedRecord mockedRecord) {
        recordLinkedList.add(mockedRecord);
    }

    public Object invoke(Object[] args) {
        MockedRecord[] mockedRecords = recordLinkedList.toArray(new MockedRecord[0]);
        if (step == mockedRecords.length) {
            throw new TooManyInvocationException("Method [" + method.getName() + "] want invoke [" + mockedRecords.length + "] times but too many invocation.");
        }
        MockedRecord mockedRecord = mockedRecords[step++];
        Object[] wantedArgs = mockedRecord.getArgs();
        args = args != null && args.length == 0 ? null : args;
        wantedArgs = wantedArgs != null && wantedArgs.length == 0 ? null : wantedArgs;
        if (wantedArgs == args) {
            return mockedRecord.getReturnValue();
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
        for (int i = 0; i < wantedArgs.length; i++) {
            if (wantedArgs[i].getClass().isPrimitive()) {
                if (wantedArgs[i].equals(args)) {
                    continue;
                }
                throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want " + Arrays.toString(wantedArgs) + " but inputted " + Arrays.toString(args));
            }
            if (!(wantedArgs[i] instanceof Integer)) {
                // TODO replace the fields in this argument(this argument is created by the tested method).
                continue;
            }
            if (!wantedArgs[i].equals(args[i])) {
                throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want " + Arrays.toString(wantedArgs) + " but inputted " + Arrays.toString(args));
            }
        }
        return mockedRecord.getReturnValue();
    }

    public void checkInvocationTimes() {
        if (step != recordLinkedList.size()) {
            throw new TooLessInvocationException("Method [" + method.getName() + "] want invoke [" + recordLinkedList.size() + "] times but invoked [" + step + "] times.");
        }
    }

    public Method getMethod() {
        return method;
    }
}
