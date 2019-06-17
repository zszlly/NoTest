package com.github.zszlly;

import com.github.zszlly.annotation.NoTest;
import com.github.zszlly.model.Case;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;

public class DummyMain {

    public static void main(String[] args) {
        Collection<Case> cases = new LinkedList<>();
        cases.add(new Case(new Object[]{1, 2}, 3));
        cases.add(new Case(new Object[]{2, 3}, 5));
        NoTestPlayer player = new NoTestPlayer(cases, DummyMain.class);
        player.play();
    }

    private static String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @NoTest
    private int add(int a, int b) {
        return a + b;
    }

}
