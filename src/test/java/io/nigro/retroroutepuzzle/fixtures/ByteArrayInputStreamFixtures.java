package io.nigro.retroroutepuzzle.fixtures;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ByteArrayInputStreamFixtures {

    private final static String path = "./src/test/resources/txt/";

    public static ByteArrayInputStream getByteArrayInputStreamResult(String filename) {
        File file = new File(String.format("%s%s.txt", path, filename));
        if (!file.exists()) {
            throw new IllegalStateException();
        }
        try {
            return new ByteArrayInputStream(new String(FileUtils.readFileToByteArray(file), StandardCharsets.UTF_8)
                    .replaceAll("\\r\\n", "\n")
                    .replaceAll("\\r", "\n")
                    .getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }
}
