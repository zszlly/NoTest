package com.github.zszlly.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NoTestProxy implements MethodInterceptor {

    private Object object;
    private Map<Object, Integer> map;

    private NoTestProxy(Object object, Map<Object, Integer> map) {
        this.object = object;
        this.map = map;
    }

    public static <T> T proxy(T t, Map<Object, Integer> map) {
        Enhancer e = new Enhancer();
        e.setSuperclass(t.getClass());
        e.setInterfaces(new Class[] {Integer.class});
        e.setCallback(new NoTestProxy(t, map));
        Object proxiedT = e.create();
        map.put(t, (Integer) proxiedT);
        return (T) proxiedT;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object[] proxiedArgs = args == null ? null : Arrays.stream(args)
                .map(this::map)
                .toArray();
        return map(method.invoke(object, proxiedArgs));
    }

    private Object map(Object origin) {
        if (origin == null) {
            return null;
        }
        if (origin.getClass().isPrimitive()) {
            return origin;
        }
        if (map.containsKey(origin)) {
            return map.get(origin);
        }
        return proxy(origin, map);
    }

}
