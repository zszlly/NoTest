package com.github.zszlly;

import com.github.zszlly.model.Case;

import java.util.Collection;

public interface CaseLoader {

    Collection<Case> getCases(String methodName);

}
