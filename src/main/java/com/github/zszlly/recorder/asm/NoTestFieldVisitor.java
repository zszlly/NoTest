package com.github.zszlly.recorder.asm;

import jdk.internal.org.objectweb.asm.FieldVisitor;

public class NoTestFieldVisitor extends FieldVisitor {

    static final String CASE_SAVER_FIELD_NAME = "_noTest_caseSaver_field";

    public NoTestFieldVisitor(int api) {
        super(api);
    }

    public NoTestFieldVisitor(int api, FieldVisitor fv) {
        super(api, fv);
    }
}
