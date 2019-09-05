package com.github.zszlly.agent;

import java.util.*;
import jdk.internal.org.objectweb.asm.*;

public class GeneratedClassADump implements Opcodes {

    public static byte[] dump() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/github/zszlly/agent/GeneratedClassA", null, "java/lang/Object", null);

        cw.visitInnerClass("java/lang/invoke/MethodHandles$Lookup", "java/lang/invoke/MethodHandles", "Lookup", ACC_PUBLIC + ACC_FINAL + ACC_STATIC);

        {
            fv = cw.visitField(ACC_PRIVATE, "field", "Lcom/github/zszlly/builder/CaseBuilder;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "boxing", "(ZCBSIJFD)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(ILOAD, 5);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(LLOAD, 6);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(FLOAD, 8);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
            mv.visitInsn(POP);
            mv.visitVarInsn(DLOAD, 9);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 11);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "unboxing", "(Ljava/lang/Boolean;Ljava/lang/Character;Ljava/lang/Byte;Ljava/lang/Short;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Float;Ljava/lang/Double;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
            mv.visitVarInsn(ISTORE, 9);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
            mv.visitVarInsn(ISTORE, 10);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
            mv.visitVarInsn(ISTORE, 11);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
            mv.visitVarInsn(ISTORE, 12);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
            mv.visitVarInsn(ISTORE, 13);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
            mv.visitVarInsn(LSTORE, 14);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
            mv.visitVarInsn(FSTORE, 16);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
            mv.visitVarInsn(DSTORE, 17);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 19);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "invokeVirtual", "()V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "java/util/LinkedList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getField", "()V", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitFieldInsn(GETSTATIC, "com/github/zszlly/util/NoTestUtils", "VOID_ARGUMENT", "Lcom/github/zszlly/model/VoidArgument;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "out", "()V", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("123");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "test", "(IDLjava/lang/String;Ljava/lang/Float;)D", null, null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(I2D);
            mv.visitVarInsn(DLOAD, 2);
            mv.visitInsn(DADD);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "parseDouble", "(Ljava/lang/String;)D", false);
            mv.visitInsn(DADD);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
            mv.visitInsn(F2D);
            mv.visitInsn(DADD);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(4, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getRunnable", "()Ljava/lang/Object;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInvokeDynamicInsn("run", "(Lcom/github/zszlly/agent/GeneratedClassA;)Ljava/lang/Runnable;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"), new Object[]{Type.getType("()V"), new Handle(Opcodes.H_INVOKESPECIAL, "com/github/zszlly/agent/GeneratedClassA", "lambda$getRunnable$0", "()V"), Type.getType("()V")});
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "listAdd", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "java/util/LinkedList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedList", "add", "(Ljava/lang/Object;)Z", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "createRecord", "(Ljava/lang/reflect/Method;)V", null, new String[] { "java/lang/NoSuchMethodException" });
            mv.visitCode();
            mv.visitTypeInsn(NEW, "com/github/zszlly/model/MethodHolder");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "com/github/zszlly/model/MethodHolder", "<init>", "(Ljava/lang/reflect/Method;)V", false);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitTypeInsn(NEW, "java/util/LinkedList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 3);
            mv.visitTypeInsn(NEW, "com/github/zszlly/model/VoidArgument");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/github/zszlly/model/VoidArgument", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitTypeInsn(NEW, "com/github/zszlly/model/Record");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "com/github/zszlly/model/Record", "<init>", "(Lcom/github/zszlly/model/MethodHolder;Ljava/util/List;Lcom/github/zszlly/model/Argument;)V", false);
            mv.visitVarInsn(ASTORE, 5);
            mv.visitInsn(RETURN);
            mv.visitMaxs(5, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "cast", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
            mv.visitVarInsn(ASTORE, 2);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "readArgs", "([Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, 3);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 4);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_SYNTHETIC, "lambda$getRunnable$0", "()V", null, null);
            mv.visitCode();
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 1);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, "com/github/zszlly/builder/CaseBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/github/zszlly/builder/CaseBuilder", "<init>", "()V", false);
            mv.visitFieldInsn(PUTFIELD, "com/github/zszlly/agent/GeneratedClassA", "field", "Lcom/github/zszlly/builder/CaseBuilder;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
