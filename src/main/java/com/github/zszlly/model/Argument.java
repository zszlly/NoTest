package com.github.zszlly.model;

import java.util.Objects;

public abstract class Argument {

    protected final Integer instanceId;

    public Argument(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return Objects.equals(instanceId, argument.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId);
    }

    @Override
    public String toString() {
        return "Argument{" +
                "instanceId=" + instanceId +
                '}';
    }
}
