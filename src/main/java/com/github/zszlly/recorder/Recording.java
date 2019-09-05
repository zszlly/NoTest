package com.github.zszlly.recorder;

import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.mark.ProxiedInstance;
import com.github.zszlly.recorder.proxy.NoTestActionRecorder;
import com.github.zszlly.util.ClassUtils;
import com.github.zszlly.util.NoTestUtils;
import net.sf.cglib.proxy.Enhancer;

import java.util.Map;

public class Recording {

    public static Object proxyInstance(CaseBuilder caseBuilder, Object instance) {
        if (instance == null) {
            return null;
        }
        if (instance instanceof ProxiedInstance || ClassUtils.isPrimitive(instance)) {
            return instance;
        }
        Map<Integer, Class<?>> mockedInstanceClassTable = caseBuilder.getMockedInstanceClassTable();
        Class<?> clazz = instance.getClass();
        Enhancer e = new Enhancer();
        if (NoTestUtils.isLambda(clazz)) {
            Class<?> interfaceClass = clazz.getInterfaces()[0];
            mockedInstanceClassTable.put(instance.hashCode(), interfaceClass);
            e.setInterfaces(new Class[]{interfaceClass, ProxiedInstance.class});
        } else {
            mockedInstanceClassTable.put(instance.hashCode(), clazz);
            e.setSuperclass(clazz);
            e.setInterfaces(new Class[]{ProxiedInstance.class});
        }
        e.setCallback(new NoTestActionRecorder(instance, caseBuilder));
        return e.create();
    }



}
