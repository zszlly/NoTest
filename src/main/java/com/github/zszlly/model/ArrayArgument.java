package com.github.zszlly.model;

import com.github.zszlly.util.NoTestUtils;
import jdk.internal.org.objectweb.asm.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayArgument extends Argument {

    private final List<Argument> list;

    public ArrayArgument(Integer instanceId, Object arr) {
        super(instanceId);
        int len = Array.getLength(arr);
        list = new ArrayList<>(len);
        for (int i = 0; i < len; ++i) {
            list.add(NoTestUtils.arrayElementToArgument(arr, i));
        }
    }

}
