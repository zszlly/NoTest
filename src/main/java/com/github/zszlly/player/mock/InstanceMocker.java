package com.github.zszlly.player.mock;

import com.github.zszlly.util.NoTestUtils;
import com.github.zszlly.mark.SpiedInstance;
import com.github.zszlly.model.Record;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class InstanceMocker implements MethodInterceptor {

    private final int instanceId;
    private final Map<Integer, InstanceMocker> mockedObjectMap;
    private Map<Method, MethodMocker> methodMockerMap = new HashMap<>();
    private Object instance = null;

    InstanceMocker(int instanceId, Map<Integer, InstanceMocker> mockedObjectMap) {
        this.instanceId = instanceId;
        this.mockedObjectMap = mockedObjectMap;
    }

    public static InstanceMocker mock(Integer id, Class<?> tClass, Map<Integer, InstanceMocker> mockedObjectMap) {
        if (NoTestUtils.isPrimitive(tClass)) {
            throw new IllegalArgumentException("Unsupported mocked class: " + tClass);
        }
        InstanceMocker mocker = new InstanceMocker(id, mockedObjectMap);
        Enhancer e = new Enhancer();
        if (tClass.isInterface()) {
            e.setInterfaces(new Class[]{tClass, SpiedInstance.class});
        } else {
            e.setSuperclass(tClass);
            e.setInterfaces(new Class[]{SpiedInstance.class});
        }
        e.setCallback(mocker);
        mocker.setInstance(e.create());
        return mocker;
    }

    public void addRecord(Record mockedRecord) {
        methodMockerMap.computeIfAbsent(mockedRecord.getMethod(), method -> new MethodMocker(method, mockedObjectMap)).addRecord(mockedRecord);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        if ("getInstanceId".equals(method.getName())) {
            return instanceId;
        }
        return methodMockerMap.get(method).invoke(args);
    }

    public void checkInvocation() {
        methodMockerMap.values().forEach(MethodMocker::checkInvocationTimes);
    }

    public int getInstanceId() {
        return instanceId;
    }

    public Object getInstance() {
        return instance;
    }

    private void setInstance(Object instance) {
        this.instance = instance;
    }
}
