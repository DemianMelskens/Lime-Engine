package org.lime.core.serialization;

import org.lime.core.utils.Assets;

import java.io.IOException;
import java.io.OutputStream;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class YamlWriter implements AutoCloseable {
    private static final int TAB_SPACES = 2;
    private static final String START_OF_FILE = "---";
    private static final String LIST_ITEM = "-";
    private final OutputStream outputStream;
    private int indents;

    private int activeObjects;
    private int activeLists;

    public YamlWriter(String filePath) throws IOException {
        this.outputStream = Assets.getOutputStream(filePath);
        this.indents = 0;
        this.activeObjects = 0;
        this.activeLists = 0;
        init();
    }

    private void init() throws IOException {
        outputStream.write(String.format("%s%n", START_OF_FILE).getBytes());
    }

    public void pushObject(String name) throws IOException {
        outputStream.write(String.format("%s%s:%n", indent(indents), name).getBytes());
        indents++;
        activeObjects++;
    }

    public void popObject() {
        indents--;
        LM_CORE_ASSERT(activeObjects > 0, "Trying to pop an object while non is being written!");
        activeObjects--;
    }

    public void addField(String key, boolean value) throws IOException {
        outputStream.write(String.format("%s%s: %s%n", indent(indents), key, value).getBytes());
    }

    public void addField(String key, int value) throws IOException {
        outputStream.write(String.format("%s%s: %d%n", indent(indents), key, value).getBytes());
    }

    public void addField(String key, float value) throws IOException {
        outputStream.write(String.format("%s%s: %f%n", indent(indents), key, value).getBytes());
    }

    public void addField(String key, String value) throws IOException {
        outputStream.write(String.format("%s%s: \"%s\"%n", indent(indents), key, value).getBytes());
    }

    public void pushList(String key) throws IOException {
        outputStream.write(String.format("%s%s:%n", indent(indents), key).getBytes());
        indents++;
        activeLists++;
    }

    public void popList() {
        indents--;
        LM_CORE_ASSERT(activeLists > 0, "Trying to pop a list while non is being written!");
        activeLists--;
    }

    public void addListItem(boolean value) throws IOException {
        outputStream.write(String.format("%s%s %s%n", indent(indents), LIST_ITEM, value).getBytes());
    }

    public void addListItem(int value) throws IOException {
        outputStream.write(String.format("%s%s %d%n", indent(indents), LIST_ITEM, value).getBytes());
    }

    public void addListItem(float value) throws IOException {
        outputStream.write(String.format("%s%s %f%n", indent(indents), LIST_ITEM, value).getBytes());
    }

    public void addListItem(String value) throws IOException {
        outputStream.write(String.format("%s%s \"%s\"%n", indent(indents), LIST_ITEM, value).getBytes());
    }

    public void pushListItemObject(String name) throws IOException {
        outputStream.write(String.format("%s%s %s:%n", indent(indents), LIST_ITEM, name).getBytes());
        indents += 2;
        activeObjects++;
    }

    public void popListItemObject() {
        indents -= 2;
        LM_CORE_ASSERT(activeObjects > 0, "Trying to pop an object while non is being written!");
        activeObjects--;
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws Exception {
        LM_CORE_ASSERT(activeObjects == 0, "Not all objects have been popped!");
        LM_CORE_ASSERT(activeLists == 0, "Not all lists have been popped!");
        flush();
        outputStream.close();
    }

    private String indent(int amount) {
        return " ".repeat(TAB_SPACES * amount);
    }
}
