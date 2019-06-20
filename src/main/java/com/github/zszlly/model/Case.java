package com.github.zszlly.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Case {

    private Map<Integer, Class<?>> mockedInstanceClassTable;
    private Map<Integer, String> primitiveInstanceTable;
    private Map<String, Integer> fieldTable;
    private Action[] actions;
    private Record record;

    public Case(Map<Integer, Class<?>> mockedInstanceClassTable, Map<Integer, String> primitiveInstanceTable, Map<String, Integer> fieldTable, Action[] actions, Record record) {
        this.mockedInstanceClassTable = mockedInstanceClassTable;
        this.primitiveInstanceTable = primitiveInstanceTable;
        this.fieldTable = fieldTable;
        this.actions = actions;
        this.record = record;
    }

    public Map<Integer, Class<?>> getMockedInstanceClassTable() {
        return mockedInstanceClassTable;
    }

    public Map<Integer, String> getPrimitiveInstanceTable() {
        return primitiveInstanceTable;
    }

    public Map<String, Integer> getFieldTable() {
        return fieldTable;
    }

    public Action[] getActions() {
        return actions;
    }

    public Record getRecord() {
        return record;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return Objects.equals(mockedInstanceClassTable, aCase.mockedInstanceClassTable) &&
                Objects.equals(primitiveInstanceTable, aCase.primitiveInstanceTable) &&
                Objects.equals(fieldTable, aCase.fieldTable) &&
                Arrays.equals(actions, aCase.actions) &&
                Objects.equals(record, aCase.record);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mockedInstanceClassTable, primitiveInstanceTable, fieldTable, record);
        result = 31 * result + Arrays.hashCode(actions);
        return result;
    }
}
