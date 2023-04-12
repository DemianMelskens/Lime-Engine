package org.lime.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class Assets {

    public static FileInputStream getInputStream(String relativeFilePath) {
        try {
            return new FileInputStream("assets/" + relativeFilePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open inputstream to file '%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }

    public static FileOutputStream getOutputStream(String relativeFilePath) {
        try {
            return new FileOutputStream("assets/" + relativeFilePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open outputstream to file'%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }

    public static byte[] load(String relativeFilePath) {
        try {
            return getInputStream(relativeFilePath).readAllBytes();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Could not load file '%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }

    public static String loadContent(String relativeFilePath) {
        return new String(load(relativeFilePath));
    }

    public static List<String> loadContentLines(String relativeFilePath) {
        try {
            return Files.readAllLines(Path.of("assets/" + relativeFilePath));
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Could not load file '%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }
}
