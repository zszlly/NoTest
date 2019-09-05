package com.github.zszlly.recorder.asm;

import com.github.zszlly.builder.CaseBuilder;
import com.github.zszlly.recorder.Recording;

import java.util.Arrays;

public class NoTestASMRecorder {

    public static Object[] proxyArgs(Object... args) {
        CaseBuilder builder = (CaseBuilder) args[0];
        return Arrays.stream(args)
                .filter(arg -> arg != builder)
                .map(arg -> Recording.proxyInstance(builder, arg))
                .toArray();
    }

}
