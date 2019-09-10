package com.github.zszlly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.io.CaseHolder;
import com.github.zszlly.io.CaseLoader;
import com.github.zszlly.io.CaseSaver;
import com.github.zszlly.model.Case;
import com.github.zszlly.player.NoTestPlayer;
import com.github.zszlly.recorder.NoTestRecorder;
import com.github.zszlly.recorder.asm.ASMCaseSaver;
import com.github.zszlly.recorder.asm.NoTestClassVisitor;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static jdk.internal.org.objectweb.asm.ClassReader.EXPAND_FRAMES;

public class DummyMain extends ClassLoader {

//    private int a;

    public static void main(String[] args) throws Throwable {
        testRecord();
    }

    private static void testRecord() throws Throwable {

        System.out.println("test record");

        ClassReader cr = new ClassReader(DummyMain.class.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        NoTestClassVisitor pt = new NoTestClassVisitor(Opcodes.ASM5, cw);
        cr.accept(pt, EXPAND_FRAMES);
        byte[] bytes = cw.toByteArray();
//        CheckClassAdapter.verify(new ClassReader(bytes), true, new PrintWriter(System.out));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ASMCaseSaver.init(out);

        Class<?> clazz = new DummyMain().defineClass(null, bytes, 0, bytes.length);
        Object obj = clazz.newInstance();
        Method add = clazz.getDeclaredMethod("add", int.class, int.class);
        add.invoke(obj, 1, 2);
        Method addB = clazz.getDeclaredMethod("addB", int.class, GetB.class);
        addB.invoke(obj, 2, (GetB) () -> 3);
        Method addArray = clazz.getDeclaredMethod("addArray", Integer[].class);
        addArray.invoke(obj, new Object[] {new Integer[] {1, 2, 3, 4, 5}});
//
//        CaseHolderImpl caseHolderImpl = new CaseHolderImpl();
//        DummyMain testDummyMain = NoTestRecorder.record(new DummyMain(), caseHolderImpl);
//        testDummyMain.add(1, 2);
//        testDummyMain.addB(2, () -> 3);
//        testDummyMain.a = 3;
//        testDummyMain.addAAndB(() -> 4);
        ObjectMapper mapper = new ObjectMapper();
        String caseJson = ASMCaseSaver.toJsonString();
        System.out.println(caseJson);
        System.out.println("test playback");
        Collection<Case> cases = mapper.readValue(caseJson, CaseHolder.class).getCases();
        NoTestPlayer player = new NoTestPlayer(cases, DummyMain.class);
        player.play();
    }

    @NoTest
    public int add(int a, int b) {
        int value = a + b;
        System.out.println(value);
        return value;
    }

    @NoTest
    public int addB(int a, GetB getB) {
        int value = a + getB.getBFn();
        System.out.println(value);
        return value;
    }

    @NoTest
    public int addArray(Integer[] arr) {
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        return sum;
    }

//    @NoTest
//    public int addAAndB(GetB getB) {
//        int value = a + getB.getBFn();
//        System.out.println(value);
//        return value;
//    }

    public interface GetB {
        int getBFn();
    }

    private static class CaseHolderImpl implements CaseSaver, CaseLoader {

        private static final String CASE_FILE_PATH = "cases.json";
        private static final ObjectMapper MAPPER = new ObjectMapper();

        private List<Case> caseList = new LinkedList<>();

        @Override
        public void addCase(Case c) {
            caseList.add(c);
        }

        @Override
        public void addCases(Collection<Case> cases) {
            caseList.addAll(cases);
        }

        @Override
        public void save() {
            try {
                File file = new File(CASE_FILE_PATH);
                MAPPER.writeValue(file, caseList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void load() {

        }

        @Override
        public CaseHolder getCaseHolder() {
            return new CaseHolder(caseList);
        }
    }

}
