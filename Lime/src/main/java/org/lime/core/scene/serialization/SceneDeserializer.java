package org.lime.core.scene.serialization;

import org.lime.core.scene.Scene;
import org.lime.core.serialization.YamlReader;

import java.io.IOException;

public class SceneDeserializer {

    private YamlReader yamlReader;

    public SceneDeserializer(String filePath) {
        try {
            this.yamlReader = new YamlReader(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scene deserialize() {
        Scene scene = new Scene();
        yamlReader.read();
        return scene;
    }
}
