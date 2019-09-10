package com.github.zszlly.recorder.asm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zszlly.io.CaseHolder;
import com.github.zszlly.model.Case;
import com.github.zszlly.util.SneakyThrow;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ASMCaseSaver {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static OutputStream out;
    private static final List<Case> LIST = new LinkedList<>();

    static {
        MAPPER.writerFor(new TypeReference<List>(){});
    }

    public static void init(String path) throws FileNotFoundException {
        out = new FileOutputStream(new File(path));
    }

    public static void init(OutputStream _out) {
        out = _out;
    }

    public static void saveCase(Case c) {
        LIST.add(c);
    }

    public static String toJsonString() {
        try {
            return MAPPER.writeValueAsString(new CaseHolder(LIST));
        } catch (IOException e) {
            SneakyThrow.sneakyThrow(e);
            return null;
        }
    }

}
