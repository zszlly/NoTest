package com.github.zszlly.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MockedRecord {

    private Method method;
    private Object[] args;
    private Object returnValue;

    public MockedRecord(Method method, Object[] args, Object returnValue) {
        this.method = method;
        this.args = args;
        this.returnValue = returnValue;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockedRecord that = (MockedRecord) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(args, that.args) &&
                Objects.equals(returnValue, that.returnValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, returnValue);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }
}
