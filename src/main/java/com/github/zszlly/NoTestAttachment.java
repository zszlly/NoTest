package com.github.zszlly;

import com.sun.tools.attach.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NoTestAttachment {

    public static void main(String[] args) throws Throwable {
        System.out.print("input process pid: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String pid = reader.readLine();
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent("D:\\Suit\\Documents\\Workspace\\Java\\no-test\\build\\libs\\no-test.jar");
        vm.detach();
    }

}
