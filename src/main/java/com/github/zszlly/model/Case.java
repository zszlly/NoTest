package com.github.zszlly.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Case {

    private final Map<Integer, Class<?>> mockedInstanceClassTable;
    private final Map<String, Argument> fieldTable;
    private final List<Action> actions;
    private final Record record;

    public Case(Map<Integer, Class<?>> mockedInstanceClassTable, Map<String, Argument> fieldTable, List<Action> actions, Record record) {
        this.mockedInstanceClassTable = mockedInstanceClassTable;
        this.fieldTable = fieldTable;
        this.actions = actions;
        this.record = record;
    }

    public Map<Integer, Class<?>> getMockedInstanceClassTable() {
        return mockedInstanceClassTable;
    }

    public Map<String, Argument> getFieldTable() {
        return fieldTable;
    }

    public List<Action> getActions() {
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
                Objects.equals(fieldTable, aCase.fieldTable) &&
                Objects.equals(actions, aCase.actions) &&
                Objects.equals(record, aCase.record);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mockedInstanceClassTable, fieldTable, actions, record);
    }

    @Override
    public String toString() {
        return "Case{" +
                "mockedInstanceClassTable=" + mockedInstanceClassTable +
                ", fieldTable=" + fieldTable +
                ", actions=" + actions +
                ", record=" + record +
                '}';
    }
}
