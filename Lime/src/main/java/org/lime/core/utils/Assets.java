package org.lime.core.utils;

import java.io.*;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class Assets {

    public static FileInputStream getInputStream(String relativeFilePath) {
        try {
            return new FileInputStream("assets/" + relativeFilePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open InputStream to file '%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }

    public static FileOutputStream getOutputStream(String relativeFilePath) {
        try {
            return new FileOutputStream("assets/" + relativeFilePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open OutputStream to file'%s', reason: %s", relativeFilePath, e.getMessage()));
        }
    }

    public static FileWriter getFileWriter(String relativeFilePath) {
        try {
            return new FileWriter("assets/" + relativeFilePath);
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open FileWriter to file'%s', reason: %s", relativeFilePath, e.getMessage()));
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
}
