package com.github.zszlly.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RecordBuilder {

    private Method method;
    private List<Object> args = new ArrayList<>();
    private Object returnValue;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void addArg(Object arg) {
        args.add(arg);
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
