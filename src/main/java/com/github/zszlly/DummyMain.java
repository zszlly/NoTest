package com.github.zszlly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.io.CaseLoader;
import com.github.zszlly.io.CaseSaver;
import com.github.zszlly.model.Case;
import com.github.zszlly.player.NoTestPlayer;
import com.github.zszlly.recorder.NoTestRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DummyMain {

    private int a;

    public static void main(String[] args) throws Throwable {
        testRecord();
    }

    private static void testRecord() throws Throwable {
        System.out.println("test record");
        CaseHolder caseHolder = new CaseHolder();
        DummyMain testDummyMain = NoTestRecorder.record(new DummyMain(), caseHolder);
        testDummyMain.add(1, 2);
        testDummyMain.addB(2, () -> 3);
        testDummyMain.a = 3;
        testDummyMain.addAAndB(() -> 4);
        System.out.println("test playback");
        NoTestPlayer player = new NoTestPlayer(caseHolder.getCases(), DummyMain.class);
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
    public int addAAndB(GetB getB) {
        int value = a + getB.getBFn();
        System.out.println(value);
        return value;
    }

    public interface GetB {
        int getBFn();
    }

    private static class CaseHolder implements CaseSaver, CaseLoader {

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

        public Collection<Case> getCases() {
            return caseList;
        }
    }

}
