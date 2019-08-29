package com.github.zszlly.agent;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;

public class NoTestAgent extends ClassLoader {

    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException, InterruptedException, IOException {
//        test(inst);
    }

    private static void test(Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException, IOException {
        String className = "com.github.zszlly.DummyMain";
        Class<?> clazz = Class.forName(className);
        ClassReader cr = new ClassReader(className);
        ClassWriter cw = new ClassWriter(0);
        TraceClassVisitor cv = new TraceClassVisitor(cw, new PrintWriter(System.out));
        cr.accept(cv, 0);
    }

    private static void getArgs() throws Throwable {
        ClassReader cr = new ClassReader(TestClass.class.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        NoTestClassVisitor pt = new NoTestClassVisitor(Opcodes.ASM5, cw);
        cr.accept(pt, 0);
        byte[] bytes = cw.toByteArray();
        Class<?> clazz = new NoTestAgent().defineClass(null, bytes, 0, bytes.length);
        Object obj = clazz.newInstance();
        Method method1 = clazz.getDeclaredMethod("test1", int.class, Object.class);
        method1.invoke(obj, 1, "2");
        Method method2 = clazz.getDeclaredMethod("test2", float.class, Object.class);
        method2.invoke(obj, (float) 3.0, "4");
    }

    public static void main(String[] args) throws Throwable {
//        ASMifier.main(new String[]{GeneratedClassA.class.getName()});
//        test(null);
//        System.out.println(Arrays.stream(Type.getArgumentTypes("(Ljava/lang/String;Ljava/lang/instrument/Instrumentation;)V")).map(Type::getClassName).collect(Collectors.toList()));
//        ProxyTest.proxy(DummyMain.class);
        getArgs();
    }

}
