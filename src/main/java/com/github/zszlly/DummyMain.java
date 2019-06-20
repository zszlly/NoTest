package com.github.zszlly;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.mock.MockedClassMark;
import com.github.zszlly.model.Action;
import com.github.zszlly.model.Case;
import com.github.zszlly.model.Record;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DummyMain {

    private int a;

    public static void main(String[] args) throws Throwable {
        Collection<Case> cases = new LinkedList<>();
        Map<Integer, Class<?>> mockedObjects = new HashMap<>();
        mockedObjects.put(1, int.class);
        mockedObjects.put(2, int.class);
        mockedObjects.put(3, int.class);
        mockedObjects.put(4, int.class);
        mockedObjects.put(5, int.class);
        mockedObjects.put(10, GetB.class);

        Method getBMethod = GetB.class.getDeclaredMethod("getBFn");
        Method addAAndBMethod = DummyMain.class.getDeclaredMethod("addAAndB", GetB.class);
        Map<String, Integer> fieldTable = new HashMap<>();
        fieldTable.put("a", 1);
        Map<Integer, String> primitiveInstanceTable = new HashMap<>();
        primitiveInstanceTable.put(1, "1");
        primitiveInstanceTable.put(2, "2");
        primitiveInstanceTable.put(3, "3");
        primitiveInstanceTable.put(4, "4");
        primitiveInstanceTable.put(5, "5");
        cases.add(new Case(mockedObjects, primitiveInstanceTable, fieldTable, new Action[]{new Action(10, new Record(getBMethod, null, () -> 2))}, new Record(addAAndBMethod, new MockedClassMark[]{() -> 10}, () -> 3)));
        cases.add(new Case(mockedObjects, primitiveInstanceTable, fieldTable, new Action[]{new Action(10, new Record(getBMethod, null, () -> 4))}, new Record(addAAndBMethod, new MockedClassMark[]{() -> 10}, () -> 5)));
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

    @NoTest
    private int addAAndB(GetB getB) {
        return a + getB.getBFn();
    }

    public interface GetB {
        int getBFn();
    }

}
