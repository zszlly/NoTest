package com.github.zszlly.model;

public class MockedArgument extends Argument {

    public MockedArgument(Integer instanceId) {
        super(instanceId);
    }

    @Override
    public String toString() {
        return "MockedArgument{" +
                "instanceId=" + instanceId +
                '}';
    }
}
