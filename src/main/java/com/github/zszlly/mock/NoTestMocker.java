package com.github.zszlly.mock;

import com.github.zszlly.model.Record;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoTestMocker implements MethodInterceptor {

    private int instanceId;
    private Map<Method, MethodMocker> methodMockerMap = new HashMap<>();
    private Object object;

    public NoTestMocker(int instanceId) {
        this.instanceId = instanceId;
    }

    public void addInvocationRecord(Record record) {
        methodMockerMap.computeIfAbsent(record.getMethod(), MethodMocker::new).addRecord(record);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        if (obj instanceof MockedClassMark && MockedClassMark.INSTANCE_ID.equals(method.getName())) {
            return instanceId;
        }
        return methodMockerMap.get(method).invoke(args);
    }

    public void checkInvocation() {
        methodMockerMap.values().forEach(MethodMocker::checkInvocationTimes);
    }

    public static <T> T mock(Integer id, Class<T> tClass, Map<Integer, NoTestMocker> mockedObject) {
        NoTestMocker mocker = new NoTestMocker(id);
        Enhancer e = new Enhancer();
        if (tClass.isInterface()) {
            e.setInterfaces(new Class[]{tClass, MockedClassMark.class});
        } else {
            e.setSuperclass(tClass);
            e.setInterfaces(new Class[]{MockedClassMark.class});
        }
        e.setCallback(mocker);
        mockedObject.put(id, mocker);
        T t = (T) e.create();
        mocker.setObject(t);
        return t;
    }

    private void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
