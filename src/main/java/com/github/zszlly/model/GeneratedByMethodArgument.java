package com.github.zszlly.model;

import java.util.Objects;

public class GeneratedByMethodArgument extends Argument {

    private final Class<?> objectClass;

    public GeneratedByMethodArgument(Class<?> objectClass) {
        super(-1);
        this.objectClass = objectClass;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeneratedByMethodArgument that = (GeneratedByMethodArgument) o;
        return Objects.equals(objectClass, that.objectClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), objectClass);
    }

    @Override
    public String toString() {
        return "GeneratedByMethodArgument{" +
                "objectClass=" + objectClass +
                ", instanceId=" + instanceId +
                '}';
    }
}
