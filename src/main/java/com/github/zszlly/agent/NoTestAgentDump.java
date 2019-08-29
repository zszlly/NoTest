package com.github.zszlly.agent;
import jdk.internal.org.objectweb.asm.*;

public class NoTestAgentDump implements Opcodes {

    public static byte[] dump () throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/github/zszlly/agent/NoTestAgent", null, "java/lang/Object", null);

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
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "agentmain", "(Ljava/lang/String;Ljava/lang/instrument/Instrumentation;)V", null, new String[] { "java/lang/ClassNotFoundException", "java/lang/instrument/UnmodifiableClassException", "java/lang/InterruptedException", "java/io/IOException" });
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "com/github/zszlly/agent/NoTestAgent", "test", "(Ljava/lang/instrument/Instrumentation;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC, "test", "(Ljava/lang/instrument/Instrumentation;)V", null, new String[] { "java/lang/ClassNotFoundException", "java/lang/instrument/UnmodifiableClassException", "java/io/IOException" });
            mv.visitCode();
            mv.visitLdcInsn("com.github.zszlly.DummyMain");
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitVarInsn(ASTORE, 2);
            mv.visitTypeInsn(NEW, "jdk/internal/org/objectweb/asm/ClassReader");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "jdk/internal/org/objectweb/asm/ClassReader", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ASTORE, 3);
            mv.visitTypeInsn(NEW, "jdk/internal/org/objectweb/asm/ClassWriter");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESPECIAL, "jdk/internal/org/objectweb/asm/ClassWriter", "<init>", "(I)V", false);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitTypeInsn(NEW, "jdk/internal/org/objectweb/asm/util/TraceClassVisitor");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitTypeInsn(NEW, "java/io/PrintWriter");
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitMethodInsn(INVOKESPECIAL, "java/io/PrintWriter", "<init>", "(Ljava/io/OutputStream;)V", false);
            mv.visitMethodInsn(INVOKESPECIAL, "jdk/internal/org/objectweb/asm/util/TraceClassVisitor", "<init>", "(Ljdk/internal/org/objectweb/asm/ClassVisitor;Ljava/io/PrintWriter;)V", false);
            mv.visitVarInsn(ASTORE, 5);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "jdk/internal/org/objectweb/asm/ClassReader", "accept", "(Ljdk/internal/org/objectweb/asm/ClassVisitor;I)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(6, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[] { "java/lang/Throwable" });
            mv.visitCode();
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitLdcInsn(Type.getType("Lcom/github/zszlly/agent/NoTestAgent;"));
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
            mv.visitInsn(AASTORE);
            mv.visitMethodInsn(INVOKESTATIC, "jdk/internal/org/objectweb/asm/util/ASMifier", "main", "([Ljava/lang/String;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}