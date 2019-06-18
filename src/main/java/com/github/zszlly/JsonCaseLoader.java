package com.github.zszlly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zszlly.model.Case;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class JsonCaseLoader implements CaseLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Map<String, List<Case>> caseSuit = new HashMap<>();

    public void load(String content) {
        try {
            ((Map<String, List<Case>>) MAPPER.readValue(content, Map.class)).forEach((methodName, cases) -> {
                caseSuit.putIfAbsent(methodName, new ArrayList<>());
                caseSuit.get(methodName).addAll(cases);
            });
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Collection<Case> getCases(String methodName) {
        return caseSuit.get(methodName);
    }

}
