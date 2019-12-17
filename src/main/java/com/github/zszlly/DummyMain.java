package com.github.zszlly;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.mark.NoTestMark;
import com.github.zszlly.util.MethodUtils;

public class DummyMain implements NoTestMark {

    private int a;

    public static void main(String[] args) throws Throwable {
        System.out.println(MethodUtils.toNoTestMethodDescription(DummyMain.class.getDeclaredMethod("add", int.class, int.class)));
        System.out.println(MethodUtils.toNoTestMethodDescription(DummyMain.class.getDeclaredMethod("addB", int.class, GetB.class)));
        System.out.println(MethodUtils.toNoTestMethodDescription(DummyMain.class.getDeclaredMethod("addArray", Integer[].class)));
        System.out.println(MethodUtils.toNoTestMethodDescription(DummyMain.class.getDeclaredMethod("addAAndB", GetB.class)));
        System.out.println("Press any key to start demo program");
        System.in.read();
        DummyMain dummyMain = new DummyMain();
        dummyMain.add(1, 2);
        dummyMain.add(2, 3);
        dummyMain.add(3, 4);
        dummyMain.add(4, 5);
        dummyMain.addB(1, () -> 2);
        dummyMain.addB(2, () -> 3);
        dummyMain.addArray(new Integer[]{1, 2, 3, 4, 5});
        dummyMain.addArray(new Integer[]{2, 3, 4, 5, 6});
        dummyMain.a = 1;
        dummyMain.addAAndB(() -> 2);
        dummyMain.a = 2;
        dummyMain.addAAndB(() -> 3);
        System.out.println("Done.");
    }

    @NoTest
    public int add(int a, int b) {
        int value = a + b;
        System.out.println(a + " + " + b + " = " + value);
        return value;
    }

    @NoTest
    public int addB(int a, GetB getB) {
        int b = getB.getBFn();
        int value = a + b;
        System.out.println(a + " + " + b + " = " + value);
        return value;
    }

    @NoTest
    public int addArray(Integer[] arr) {
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        System.out.println("sum = " + sum);
        return sum;
    }

    @NoTest
    public int addAAndB(GetB getB) {
        int b = getB.getBFn();
        int value = a + b;
        System.out.println(a + " + " + b + " = " + value);
        return value;
    }

    public interface GetB {
        int getBFn();
    }

}
