package com.github.zszlly;

import com.sun.tools.attach.VirtualMachine;

public class NoTestAttachment {

    public static void main(String[] args) throws Throwable {
        VirtualMachine vm = VirtualMachine.attach(args[0]);
        vm.loadAgent(NoTestAttachment.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        vm.detach();
    }

}
