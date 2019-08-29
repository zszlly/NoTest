package com.github.zszlly.agent;

import jdk.internal.org.objectweb.asm.Type;

public class MethodDescription {

    private final Type returnType;
    private final Type[] argumentsType;

    public MethodDescription(String methodDescription) {
        this.returnType = Type.getReturnType(methodDescription);
        this.argumentsType = Type.getArgumentTypes(methodDescription);
    }

    public Type getReturnType() {
        return returnType;
    }

    public Type[] getArgumentsType() {
        return argumentsType;
    }
}
