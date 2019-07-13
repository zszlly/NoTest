package com.github.zszlly.recorder;

import com.github.zszlly.io.CaseSaver;
import com.github.zszlly.recorder.proxy.NoTestInstanceProxy;
import net.sf.cglib.proxy.Enhancer;

import java.util.Arrays;

public class NoTestRecorder {

    private NoTestRecorder() {}

    @SuppressWarnings("unchecked")
    public static <T> T record(T instance, CaseSaver saver) {
        Class<?> clazz = instance.getClass();
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(NoTestInstanceProxy::setPermission);
        Enhancer e = new Enhancer();
        e.setSuperclass(clazz);
        NoTestInstanceProxy instanceProxy = new NoTestInstanceProxy(instance, saver);
        e.setCallback(instanceProxy);
        T proxiedInstance = (T) e.create();
        instanceProxy.setProxiedInstance(proxiedInstance);
        return proxiedInstance;
    }

}
