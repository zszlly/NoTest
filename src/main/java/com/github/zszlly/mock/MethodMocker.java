package com.github.zszlly.mock;

import com.github.zszlly.exceptions.TooLessInvocationException;
import com.github.zszlly.exceptions.TooManyInvocationException;
import com.github.zszlly.exceptions.WrongArgumentsException;
import com.github.zszlly.model.Record;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MethodMocker {

    private Method method;
    private List<Record> recordList = new LinkedList<>();
    private int step = 0;

    public MethodMocker(Method method) {
        this.method = method;
    }

    public void addRecord(Record record) {
        recordList.add(record);
    }

    public Object invoke(Object[] args) {
        Record[] records = recordList.toArray(new Record[0]);
        if (step == records.length) {
            throw new TooManyInvocationException("Method [" + method.getName() + "] want invoke [" + records.length + "] times but too many invocation.");
        }
        Record record = records[step++];
        Object[] wantedArgs = record.getArgs();
        args = args != null && args.length == 0 ? null : args;
        wantedArgs = wantedArgs != null && wantedArgs.length == 0 ? null : wantedArgs;
        if (wantedArgs == args) {
            return record.getReturnValue();
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
            if (!(wantedArgs[i] instanceof MockedClassMark)) {
                // TODO replace the fields in this argument.
                continue;
            }
            if (!wantedArgs[i].equals(args[i])) {
                throw new WrongArgumentsException("Method [" + method.getName() + "] invocation want " + Arrays.toString(wantedArgs) + " but inputted " + Arrays.toString(args));
            }
        }
        return record.getReturnValue();
    }

    public void checkInvocationTimes() {
        if (step != recordList.size()) {
            throw new TooLessInvocationException("Method [" + method.getName() + "] want invoke [" + recordList.size() + "] times but invoked [" + step + "] times.");
        }
    }

    public Method getMethod() {
        return method;
    }
}
