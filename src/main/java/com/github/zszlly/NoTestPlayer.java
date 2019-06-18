package com.github.zszlly;

import com.github.zszlly.exceptions.WrongReturnValueException;
import com.github.zszlly.model.Case;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class NoTestPlayer {

    private Collection<Case> cases;
    private Class<?> clazz;

    public NoTestPlayer(Collection<Case> cases, Class<?> clazz) {
        this.cases = cases;
        this.clazz = clazz;
    }

    public void play() {
        MethodScanner.scanMethods(clazz).forEach(method -> {
            cases.forEach(c -> {
                        Object returnValue = testMethod(method, c.getArgs());
                        if (c.getReturnValue() == null && returnValue == null) {
                            return;
                        }
                        if (c.getReturnValue() == null && returnValue != null) {
                            throw new WrongReturnValueException("Except: null, but returned: " + returnValue);
                        }
                        if (!c.getReturnValue().equals(returnValue)) {
                            throw new WrongReturnValueException("Except: " + c.getReturnValue() + ", but returned: " + returnValue);
                        }
                    }
            );
        });
    }

    private Object testMethod(Method method, Object[] args) {
        try {
            method.setAccessible(true);
            return method.invoke(clazz.getConstructor().newInstance(), args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

}
