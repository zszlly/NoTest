package com.github.zszlly.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.zszlly.io.JsonObject;
import com.github.zszlly.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodHolder implements JsonObject {

    private final String declaringClass;
    private final String methodName;
    private final List<String> argumentTypes;

    public MethodHolder(Method method) {
        this.declaringClass = method.getDeclaringClass().getName();
        this.methodName = method.getName();
        this.argumentTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
    }

    @JsonCreator
    public MethodHolder(
            @JsonProperty("declaringClass") String declaringClass,
            @JsonProperty("methodName") String methodName,
            @JsonProperty("argumentTypes") List<String> argumentTypes) {
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.argumentTypes = argumentTypes;
    }

    @JsonProperty("declaringClass")
    public String getDeclaringClass() {
        return declaringClass;
    }

    @JsonProperty("methodName")
    public String getMethodName() {
        return methodName;
    }

    @JsonProperty("argumentTypes")
    public List<String> getArgumentTypes() {
        return argumentTypes;
    }

    @JsonIgnore
    public Method getMethod() {
        try {
            return ClassUtils.getMethod(ClassUtils.forName(declaringClass), methodName, argumentTypes);
        } catch (Exception e) {
            System.out.println(toString());
            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodHolder that = (MethodHolder) o;
        return Objects.equals(declaringClass, that.declaringClass) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(argumentTypes, that.argumentTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaringClass, methodName, argumentTypes);
    }

    @Override
    public String toString() {
        return "MethodHolder{" +
                "declaringClass='" + declaringClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", argumentTypes=" + argumentTypes +
                '}';
    }
}
