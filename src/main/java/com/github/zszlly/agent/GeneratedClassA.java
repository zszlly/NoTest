package com.github.zszlly.agent;

import java.util.Arrays;

public class GeneratedClassA {

    public static void main(String[] args) {
        GeneratedClassA classA = new GeneratedClassA();
        if (args.length == 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        classA.printArgs(1, 2, "3");
        Object[] objs = new Object[100];
    }

    public void aaaaaaaaaaaaaaaaa(boolean v1, char v2, byte v3, short v4, int v5, long v6, float v7, double v8) {
        Boolean.valueOf(v1);
        Character.valueOf(v2);
        Byte.valueOf(v3);
        Short.valueOf(v4);
        Integer.valueOf(v5);
        Long.valueOf(v6);
        Float.valueOf(v7);
        Double.valueOf(v8);
    }

    public double test(int a, double b, String c, Float d) {
        return a + b + Double.parseDouble(c) + d;
    }

    public static void printArgs(Object... args) {
        System.out.println(Arrays.asList(args));
    }

}
