package org.lime.core.scene.serialization;

import org.lime.core.renderer.Color;
import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.*;
import org.lime.core.utils.Assets;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class SceneDeserializer {

    public static SceneDeserializer create() {
        return new SceneDeserializer();
    }

    private SceneDeserializer() {
    }

    public Scene deserialize(String filePath) {
        Yaml yaml = new Yaml();
        Map<String, Object> serializedScene = yaml.load(Assets.getInputStream(filePath));
        return deserializeScene(serializedScene);
    }

    private Scene deserializeScene(Map<String, Object> serializedScene) {
        Scene scene = new Scene();
        deserializeEntities(scene, serializedScene);
        return scene;
    }

    private void deserializeEntities(Scene scene, Map<String, Object> serializedScene) {
        List<Map<String, Object>> serializedEntities = (List<Map<String, Object>>) serializedScene.get("entities");

        serializedEntities.forEach(serializedEntity -> deserializeEntity(scene, serializedEntity));
    }

    private void deserializeEntity(Scene scene, Map<String, Object> serializedEntity) {
        Integer entity = (Integer) serializedEntity.get("id");
        scene.getRegistry().add(entity);
        deserializeComponents(new Entity(entity, scene), serializedEntity);
    }

    private void deserializeComponents(Entity entity, Map<String, Object> serializedEntity) {
        List<Map<String, Object>> serializedComponents = (List<Map<String, Object>>) serializedEntity.get("components");

        serializedComponents.forEach(serializedComponent -> deserializeComponent(entity, serializedComponent));
    }

    private void deserializeComponent(Entity entity, Map<String, Object> serializedComponent) {
        if (serializedComponent.get("type").equals("Tag Component"))
            deserializeTagComponent(entity, serializedComponent);

        if (serializedComponent.get("type").equals("Transform Component"))
            deserializeTransformComponent(entity, serializedComponent);

        if (serializedComponent.get("type").equals("SpriteRenderer Component"))
            deserializeSpriteRendererComponent(entity, serializedComponent);

        if (serializedComponent.get("type").equals("Camera Component"))
            deserializeCameraComponent(entity, serializedComponent);

        if (serializedComponent.get("type").equals("Native Script Component"))
            deserializeNativeScriptComponent(entity, serializedComponent);
    }

    private void deserializeTagComponent(Entity entity, Map<String, Object> serializedComponent) {
        entity.addComponent(TagComponent.class, getString(serializedComponent, "tag"));
    }

    private void deserializeTransformComponent(Entity entity, Map<String, Object> serializedComponent) {
        TransformComponent transform = entity.addComponent(TransformComponent.class);

        transform.position.x = this.getFloat(serializedComponent, "position.x");
        transform.position.y = this.getFloat(serializedComponent, "position.y");
        transform.position.z = this.getFloat(serializedComponent, "position.z");

        transform.rotation.x = this.getFloat(serializedComponent, "rotation.x");
        transform.rotation.y = this.getFloat(serializedComponent, "rotation.y");
        transform.rotation.z = this.getFloat(serializedComponent, "rotation.z");

        transform.scale.x = this.getFloat(serializedComponent, "scale.x");
        transform.scale.y = this.getFloat(serializedComponent, "scale.y");
        transform.scale.z = this.getFloat(serializedComponent, "scale.z");
    }

    private void deserializeSpriteRendererComponent(Entity entity, Map<String, Object> serializedComponent) {
        entity.addComponent(
            SpriteRendererComponent.class,
            Color.create(
                this.getFloat(serializedComponent, "color.r"),
                this.getFloat(serializedComponent, "color.g"),
                this.getFloat(serializedComponent, "color.b"),
                this.getFloat(serializedComponent, "color.a")
            )
        );
    }

    private void deserializeCameraComponent(Entity entity, Map<String, Object> serializedComponent) {
        CameraComponent cameraComponent = entity.addComponent(CameraComponent.class);

        cameraComponent.isPrimary = getBoolean(serializedComponent, "isPrimary");
        cameraComponent.hasFixedAspectRatio = getBoolean(serializedComponent, "hasFixedAspectRatio");

        cameraComponent.camera.setPerspectiveFOV(getFloat(serializedComponent, "camera.perspectiveFOV"));
        cameraComponent.camera.setPerspectiveNearClip(getFloat(serializedComponent, "camera.perspectiveNearClip"));
        cameraComponent.camera.setPerspectiveFarClip(getFloat(serializedComponent, "camera.perspectiveFarClip"));

        cameraComponent.camera.setOrthographicSize(getFloat(serializedComponent, "camera.orthographicSize"));
        cameraComponent.camera.setOrthographicNearClip(getFloat(serializedComponent, "camera.orthographicNearClip"));
        cameraComponent.camera.setOrthographicFarClip(getFloat(serializedComponent, "camera.orthographicFarClip"));
    }

    private void deserializeNativeScriptComponent(Entity entity, Map<String, Object> serializedComponent) {
        String className = get(serializedComponent, "class");
        try {
            entity.addComponent(NativeScriptComponent.class, Class.forName(className)).bind();
        } catch (ClassNotFoundException e) {
            throw LM_CORE_EXCEPTION(
                String.format("Failed to load script with name: %s!", className)
            );
        }
    }

    private <T> T getAndCast(Map<String, Object> object, String key) {
        return (T) object.get(key);
    }

    private <T> T get(Map<String, Object> object, String request) {
        List<String> keys = Arrays.stream(request.split("\\.")).collect(Collectors.toList());
        String key = keys.remove(keys.size() - 1);

        Map<String, Object> context = object;

        for (String k : keys) {
            context = this.getAndCast(context, k);
        }

        return this.getAndCast(context, key);
    }

    private String getString(Map<String, Object> object, String request) {
        return get(object, request);
    }

    private boolean getBoolean(Map<String, Object> object, String request) {
        return get(object, request);
    }

    private int getInt(Map<String, Object> object, String request) {
        Double value = get(object, request);
        return value.intValue();
    }

    private float getFloat(Map<String, Object> object, String request) {
        Double value = get(object, request);
        return value.floatValue();
    }
}
