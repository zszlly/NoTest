package com.github.zszlly.recorder.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zszlly.model.Case;
import com.github.zszlly.util.SneakyThrow;

import java.io.*;

public class ASMCaseSaver {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static OutputStream out;

    public static void init(String path) throws FileNotFoundException {
        out = new FileOutputStream(new File(path));
    }

    public static void init(OutputStream _out) {
        out = _out;
    }

    public static void saveCase(Case c) {
        try {
            MAPPER.writeValue(out, c);
        } catch (IOException e) {
            SneakyThrow.sneakyThrow(e);
        }
    }
}
