package com.github.zszlly.io;

import com.github.zszlly.model.Case;

import java.util.Collection;

public interface CaseLoader {

    void load();

    Collection<Case> getCases();

}
