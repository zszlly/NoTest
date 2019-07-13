package com.github.zszlly.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NullArgument extends Argument {

    @JsonCreator
    public NullArgument() {
        super(0);
    }

    @Override
    public String toString() {
        return "NullArgument{}";
    }
}
