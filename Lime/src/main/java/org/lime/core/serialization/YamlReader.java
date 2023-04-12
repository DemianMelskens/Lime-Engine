package org.lime.core.serialization;

import org.lime.core.utils.Assets;

import java.io.IOException;
import java.util.List;

public class YamlReader {

    private final String FIELD_REGEX = "([^\b]+): ([^\b]+)";
    private final String LIST_OBJECT_REGEX = "- ([^\b]+):";
    private final String LIST_ITEM_REGEX = "- ([^\b]+)";
    private final String OBJECT_REGEX = "([^\b]+):";

    private YamlNode context;
    private int indents;
    private List<String> lines;

    public YamlReader(String filePath) throws IOException {
        this.lines = Assets.loadContentLines(filePath);
    }

    public YamlNode read() {
        context = new YamlNode();

        for (String line : lines) {
            parseLine(line);
        }

        return context;
    }

    private void parseLine(String line) {
        if (line.matches(FIELD_REGEX)) {
            parseField(line);
        } else if (line.matches(LIST_OBJECT_REGEX)) {

        } else if (line.matches(LIST_ITEM_REGEX)) {

        } else if (line.matches(OBJECT_REGEX)) {
            String[] keyValue = line.split(":");
            String key = keyValue[0].trim();

            YamlNode child = new YamlNode(context);
            context.addChild(key, child);
            context = child;
        }
    }

    private void parseField(String line) {
        String[] keyValue = line.split(":");
        String key = keyValue[0].trim();
        String value = keyValue[1].trim().replace("\"", "");

        context.addValue(key, value);
    }

    private void isSameContext(String line) {
        // indents = same
        // indents = less
        // indents = more
    }

    private int countIndents(String line) {
        int count = 0;

        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i)))
                break;
            count++;
        }

        return count / 2;
    }
}
