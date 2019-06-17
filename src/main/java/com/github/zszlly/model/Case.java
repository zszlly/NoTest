package com.github.zszlly.model;

import java.util.Arrays;
import java.util.Objects;

public class Case {

    private Object[] args;
    private Object returnValue;

    public Case(Object[] args, Object returnValue) {
        this.args = args;
        this.returnValue = returnValue;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return Arrays.equals(args, aCase.args) &&
                Objects.equals(returnValue, aCase.returnValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(returnValue);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

}
