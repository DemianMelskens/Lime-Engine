package org.lime.core.utils;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.util.List;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_openFileDialog;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_saveFileDialog;

public class FileDialog {

    public static String openFile(String title, String description, List<String> accept) {
        return openFile(title, "", description, false, accept);
    }

    public static String openFile(String title, String defaultPath, String description, boolean multiple, List<String> accept) {
        try (MemoryStack stack = stackPush()) {
            PointerBuffer aFilterPatterns = stack.mallocPointer(accept.size());

            for (String filter : accept) {
                aFilterPatterns.put(stack.UTF8(filter));
            }

            aFilterPatterns.flip();

            return tinyfd_openFileDialog(title, defaultPath, aFilterPatterns, description, multiple);
        }
    }

    public static String saveFile(String title, String description, List<String> accept) {
        return saveFile(title, "", description, accept);
    }

    public static String saveFile(String title, String defaultPath, String description, List<String> accept) {
        try (MemoryStack stack = stackPush()) {
            PointerBuffer aFilterPatterns = stack.mallocPointer(accept.size());

            for (String filter : accept) {
                aFilterPatterns.put(stack.UTF8(filter));
            }

            aFilterPatterns.flip();

            return tinyfd_saveFileDialog(title, defaultPath, aFilterPatterns, description);
        }
    }
}
