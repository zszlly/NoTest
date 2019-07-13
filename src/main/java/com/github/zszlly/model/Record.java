package com.github.zszlly.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.zszlly.io.JsonObject;

import java.lang.reflect.Method;
import java.util.*;

public class Record implements JsonObject {

    private final Method method;
    private final List<Argument> args;
    private final Argument returnValue;

    @JsonCreator
    public Record(
            @JsonProperty("methodHolder") MethodHolder methodHolder,
            @JsonProperty("args") List<Argument> args,
            @JsonProperty("returnValue") Argument returnValue) {
        this(methodHolder.getMethod(), args, returnValue);
    }

    public Record(Method method, List<Argument> args, Argument returnValue) {
        this.method = method;
        this.args = args == null ? new LinkedList<>() : args;
        this.returnValue = returnValue;
    }

    @JsonIgnore
    public Method getMethod() {
        return method;
    }

    @JsonProperty("methodHolder")
    public MethodHolder getMethodHolder() {
        return new MethodHolder(method);
    }

    @JsonProperty("args")
    public List<Argument> getArgs() {
        return args;
    }

    @JsonProperty("returnValue")
    public Argument getReturnValue() {
        return returnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(method, record.method) &&
                Objects.equals(args, record.args) &&
                Objects.equals(returnValue, record.returnValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, args, returnValue);
    }

    @Override
    public String toString() {
        return "Record{" +
                "method=" + method +
                ", args=" + args +
                ", returnValue=" + returnValue +
                '}';
    }

}
