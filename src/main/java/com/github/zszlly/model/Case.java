package com.github.zszlly.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Case {

    private Map<Integer, Class<?>> mockedInstanceClassTable;
    private Record[] records;
    private Method method;
    private Object[] args;
    private Object returnValue;

    public Case(Map<Integer, Class<?>> mockedInstanceClassTable, Record[] records, Method method, Object[] args, Object returnValue) {
        this.mockedInstanceClassTable = mockedInstanceClassTable;
        this.records = records;
        this.method = method;
        this.args = args;
        this.returnValue = returnValue;
    }

    public Map<Integer, Class<?>> getMockedInstanceClassTable() {
        return mockedInstanceClassTable;
    }

    public Record[] getRecords() {
        return records;
    }

    public Object[] getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return Objects.equals(mockedInstanceClassTable, aCase.mockedInstanceClassTable) &&
                Arrays.equals(records, aCase.records) &&
                Objects.equals(method, aCase.method) &&
                Arrays.equals(args, aCase.args) &&
                Objects.equals(returnValue, aCase.returnValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mockedInstanceClassTable, method, returnValue);
        result = 31 * result + Arrays.hashCode(records);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }
}
