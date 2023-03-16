package org.lime.core.renderer.shader;

import java.util.HashMap;
import java.util.Map;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class ShaderLibrary {
    private Map<String, Shader> shaders = new HashMap<>();

    public Shader get(String name) {
        LM_CORE_ASSERT(contains(name), String.format("Shader with name: %s does not exists!", name));
        return shaders.get(name);
    }

    public void add(Shader shader) {
        add(shader.getName(), shader);
    }

    public void add(String name, Shader shader) {
        LM_CORE_ASSERT(!contains(name), String.format("Shader with name: %s already exists!", name));
        shaders.put(name, shader);
    }

    public Shader load(String filePath) {
        Shader shader = Shader.create(filePath);
        add(shader);
        return shader;
    }

    public Shader load(String name, String filePath) {
        Shader shader = Shader.create(filePath);
        add(name, shader);
        return shader;
    }

    public boolean contains(String name) {
        return shaders.containsKey(name);
    }

    public void tearDown() {
        shaders.values().forEach(Shader::tearDown);
    }
}
