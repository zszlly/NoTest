package com.github.zszlly.agent;

import jdk.internal.org.objectweb.asm.*;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_STATIC;

public class NoTestClassVisitor extends ClassVisitor {

    public NoTestClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if ("<init>".equals(name)) {
            return mv;
        }
        return new NoTestMethodVisitor(Opcodes.ASM5, mv, new MethodDescription(desc), (access & ACC_STATIC) != 0);
    }


}
