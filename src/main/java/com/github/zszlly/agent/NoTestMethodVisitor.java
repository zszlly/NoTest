package com.github.zszlly.agent;

import com.github.zszlly.util.SneakyThrow;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class NoTestMethodVisitor extends MethodVisitor {

    private final MethodDescription description;
    private final boolean isStatic;

    public NoTestMethodVisitor(int api, MethodVisitor mv, MethodDescription description, boolean isStatic) {
        super(api, mv);
        this.description = description;
        this.isStatic = isStatic;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        List<Class<?>> argumentsClass = Arrays.stream(description.getArgumentsType())
                .map(type -> {
                    String className = type.getClassName();
                    switch (className) {
                        case "void":
                            return void.class;
                        case "boolean":
                            return boolean.class;
                        case "char":
                            return char.class;
                        case "byte":
                            return byte.class;
                        case "short":
                            return short.class;
                        case "int":
                            return int.class;
                        case "float":
                            return float.class;
                        case "long":
                            return long.class;
                        case "double":
                            return double.class;
                    }
                    // TODO: add primitive array support
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        SneakyThrow.sneakyThrow(e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
        int argLen = argumentsClass.size();
        mv.visitIntInsn(BIPUSH, argLen);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        int localVarIndex = isStatic ? 0 : 1;
        for (int i = 0; i < argLen; ++i) {
            Class<?> clazz = argumentsClass.get(i);
            mv.visitInsn(DUP);
            mv.visitIntInsn(BIPUSH, i);
            if (clazz.isPrimitive()) {
                autoBoxing(localVarIndex, clazz);
            } else {
                mv.visitVarInsn(ALOAD, localVarIndex);
            }
            mv.visitInsn(AASTORE);
            localVarIndex += localVarLength(clazz);
        }
        mv.visitMethodInsn(INVOKESTATIC, "com/github/zszlly/agent/GeneratedClassA", "printArgs", "([Ljava/lang/Object;)V", false);
    }

    private void autoBoxing(int index, Class<?> type) {
        if (type == boolean.class) {
            mv.visitVarInsn(ILOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
        } else  if (type == char.class) {
            mv.visitVarInsn(ILOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
        } else if (type == byte.class) {
            mv.visitVarInsn(ILOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
        } else if (type == short.class) {
            mv.visitVarInsn(ILOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
        } else if (type == int.class) {
            mv.visitVarInsn(ILOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        } else if (type == long.class) {
            mv.visitVarInsn(LLOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(L)Ljava/lang/Long;", false);
        } else if (type == float.class) {
            mv.visitVarInsn(FLOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
        } else if (type == double.class) {
            mv.visitVarInsn(DLOAD, index);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        }
    }

    private static int localVarLength(Class<?> type) {
        if (type == boolean.class || type == char.class || type == byte.class || type == short.class || type == int.class || type == float.class) {
            return 1;
        } else if (type == long.class || type == double.class) {
            return 2;
        }
        return 1;
    }

}
