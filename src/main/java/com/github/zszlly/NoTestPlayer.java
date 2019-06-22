package com.github.zszlly;

import com.github.zszlly.model.Case;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class NoTestPlayer {

    private Collection<Case> cases;
    private Class<?> clazz;

    public NoTestPlayer(Collection<Case> cases, Class<?> clazz) {
        this.cases = cases;
        this.clazz = clazz;
    }

    public void play() {
        MethodScanner.scanMethods(clazz).forEach(method ->
                cases.stream()
                        .filter(c -> {
                            Method cMethod = c.getRecord().getMethod();
                            return cMethod.equals(method) && Arrays.equals(cMethod.getParameterTypes(), method.getParameterTypes());
                        })
                        .forEach(CasePlayer::runCase)
        );
    }

}
