package com.github.zszlly.recorder;

import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.model.Action;
import com.github.zszlly.model.Argument;
import com.github.zszlly.model.Record;
import com.github.zszlly.util.NoTestUtils;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class NoTestProxy implements MethodInterceptor {

    private static final String PROXY_CLASS_NAME = NoTestProxy.class.getName();
    private static final String RECODER_CLASS_NAME = NoTestRecorder.class.getName();

    private final int instanceId;
    private final Object noTestInstance;
    private final CaseBuilder caseBuilder;

    public NoTestProxy(Object noTestInstance, CaseBuilder caseBuilder) {
        try {
            noTestInstance.getClass().getDeclaredMethod("getInstanceId");
            throw new IllegalArgumentException("Impact method \"getInstanceId\" with no arg, please rename the method or change the input args.");
        } catch (NoSuchMethodException e) {
            instanceId = noTestInstance.hashCode();
            this.noTestInstance = noTestInstance;
            this.caseBuilder = caseBuilder;
        }
    }

    private static boolean needToRecord() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        for (int i = 3; i < stacks.length; ++i) {
            String className = stacks[i].getClassName();
            if (PROXY_CLASS_NAME.equals(className)) {
                return false;
            }
            if (RECODER_CLASS_NAME.equals(className)) {
                return true;
            }
        }
        throw new IllegalStateException("NoTestProxy instance not called by NoTestRecorder.");
    }

    @Override
    public Object intercept(Object spiedInstance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if ("getInstanceId".equals(method.getName())) {
            return instanceId;
        }
        // TODO record this invocation.
        Object returnValue = method.invoke(noTestInstance, args);
        if (needToRecord()) {
            Argument[] recordArgs = new Argument[args.length];
            for (int i = 0; i < args.length; i++) {
                recordArgs[i] = NoTestUtils.toArgument(args[i]);
            }
            caseBuilder.getActions().add(new Action(instanceId, new Record(method, recordArgs, NoTestUtils.toArgument(returnValue))));
            Map<Integer, Class<?>> mockedInstanceClassTable = caseBuilder.getMockedInstanceClassTable();
            int returnValueInstanceId = NoTestUtils.getInstanceId(recordArgs);
            if (!mockedInstanceClassTable.containsKey(returnValueInstanceId)) {
                return NoTestRecorder.proxyInstance(returnValue, caseBuilder);
            }
        }
        return returnValue;
    }

}
