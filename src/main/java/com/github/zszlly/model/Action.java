package com.github.zszlly.model;

import java.util.Objects;

public class Action {

    private int instanceIc;
    private Record record;

    public Action(int instanceIc, Record record) {
        this.instanceIc = instanceIc;
        this.record = record;
    }

    public int getInstanceId() {
        return instanceIc;
    }

    public Record getRecord() {
        return record;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return instanceIc == action.instanceIc &&
                Objects.equals(record, action.record);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceIc, record);
    }
}
