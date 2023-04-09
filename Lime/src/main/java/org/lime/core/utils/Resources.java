package org.lime.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class Resources {

    public static byte[] load(String filePath) {
        try (InputStream inputStream = Resources.class.getResourceAsStream(filePath)) {
            LM_CORE_ASSERT(inputStream != null, String.format("Could not load file '%s'", filePath));
            return inputStream.readAllBytes();
        } catch (IOException | FileSystemNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could not load file '%s', reason: %s", filePath, e.getCause()));
        }
    }

    public static String loadContent(String filePath) {
        return new String(load(filePath));
    }
}
