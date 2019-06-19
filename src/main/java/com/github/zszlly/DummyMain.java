package com.github.zszlly;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.mock.MockedClassMark;
import com.github.zszlly.model.Case;
import com.github.zszlly.model.Record;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DummyMain {

    public static void main(String[] args) throws Throwable {
        Collection<Case> cases = new LinkedList<>();
        Map<Integer, Class<?>> mockedObjects = new HashMap<>();
        mockedObjects.put(1, GetB.class);
        Method getBMethod = GetB.class.getDeclaredMethod("getBFn");
        Method addB = DummyMain.class.getDeclaredMethod("addB", int.class, GetB.class);
        cases.add(new Case(mockedObjects, new Record[]{new Record(1, getBMethod, null, 2)}, addB, new Object[] {1, (MockedClassMark) () -> 1}, 3));
        cases.add(new Case(mockedObjects, new Record[]{new Record(1, getBMethod, null, 3)}, addB, new Object[] {2, (MockedClassMark) () -> 1}, 4));
        NoTestPlayer player = new NoTestPlayer(cases, DummyMain.class);
        player.play();
    }

    @NoTest
    private int add(int a, int b) {
        return a + b;
    }

    @NoTest
    private int addB(int a, GetB getB) {
        return a + getB.getBFn();
    }

    public interface GetB {
        int getBFn();
    }

}
