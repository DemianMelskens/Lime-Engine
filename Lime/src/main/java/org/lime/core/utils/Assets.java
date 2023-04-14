package org.lime.core.utils;

import java.io.*;
import java.nio.file.Paths;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class Assets {

    public static FileInputStream getInputStream(String filePath) {
        try {
            if (isAbsolute(filePath)) {
                return new FileInputStream(filePath);
            }
            return new FileInputStream("assets/" + filePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open InputStream to file '%s', reason: %s", filePath, e.getMessage()));
        }
    }

    public static FileOutputStream getOutputStream(String filePath) {
        try {
            if (isAbsolute(filePath)) {
                return new FileOutputStream(filePath);
            }
            return new FileOutputStream("assets/" + filePath);
        } catch (FileNotFoundException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open OutputStream to file'%s', reason: %s", filePath, e.getMessage()));
        }
    }

    public static FileWriter getFileWriter(String filePath) {
        try {
            if (isAbsolute(filePath)) {
                return new FileWriter(filePath);
            }
            return new FileWriter("assets/" + filePath);
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Could open FileWriter to file'%s', reason: %s", filePath, e.getMessage()));
        }
    }

    public static byte[] load(String filePath) {
        try {
            return getInputStream(filePath).readAllBytes();
        } catch (IOException e) {
            throw LM_CORE_EXCEPTION(String.format("Could not load file '%s', reason: %s", filePath, e.getMessage()));
        }
    }

    public static String loadContent(String filePath) {
        return new String(load(filePath));
    }

    private static boolean isAbsolute(String filePath) {
        return Paths.get(filePath).isAbsolute();
    }
}
