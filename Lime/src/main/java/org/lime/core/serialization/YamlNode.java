package org.lime.core.serialization;

import java.util.HashMap;
import java.util.Map;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class YamlNode {
    private static final String CHILD_NOT_FOUND = "Node has no child for key: %s";
    private static final String VALUE_NOT_FOUND = "Node has no value for key: %s";
    private YamlNode parent;
    private Map<String, String> values;
    private Map<String, YamlNode> children;

    public YamlNode() {
        this(null);
    }

    public YamlNode(YamlNode parent) {
        this.parent = parent;
        values = new HashMap<>();
        children = new HashMap<>();
    }

    public YamlNode getChild(String key) {
        LM_CORE_ASSERT(hasChild(key), String.format(CHILD_NOT_FOUND, key));
        return children.get(key);
    }

    public void addChild(String key, YamlNode child) {
        children.put(key, child);
    }

    public String getValue(String key) {
        LM_CORE_ASSERT(hasValue(key), String.format(VALUE_NOT_FOUND, key));
        return values.get(key);
    }

    public void addValue(String key, String value) {
        values.put(key, value);
    }

    public boolean getBooleanValue(String key) {
        LM_CORE_ASSERT(hasValue(key), String.format(VALUE_NOT_FOUND, key));
        return Boolean.parseBoolean(values.get(key));
    }

    public int getIntValue(String key) {
        LM_CORE_ASSERT(hasValue(key), String.format(VALUE_NOT_FOUND, key));
        return Integer.parseInt(values.get(key));
    }

    public float getFloatValue(String key) {
        LM_CORE_ASSERT(hasValue(key), String.format(VALUE_NOT_FOUND, key));
        return Float.parseFloat(values.get(key));
    }

    private boolean hasChild(String key) {
        return children.containsKey(key);
    }

    private boolean hasValue(String key) {
        return values.containsKey(key);
    }
}
