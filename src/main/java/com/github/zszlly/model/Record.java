package com.github.zszlly.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class Record {

    private final Method method;
    private final Argument[] args;
    private final Argument returnValue;

    public Record(Method method, Argument[] args, Argument returnValue) {
        this.method = method;
        this.args = args;
        this.returnValue = returnValue;
    }

    public Method getMethod() {
        return method;
    }

    public Argument[] getArgs() {
        return args;
    }

    public Argument getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(method, record.method) &&
                Arrays.equals(args, record.args) &&
                Objects.equals(returnValue, record.returnValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, returnValue);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "method=" + method +
                ", args=" + Arrays.toString(args) +
                ", returnValue=" + returnValue +
                '}';
    }
}
