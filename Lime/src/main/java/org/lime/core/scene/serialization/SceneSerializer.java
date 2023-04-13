package org.lime.core.scene.serialization;

import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TagComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.utils.Assets;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneSerializer {

    private Scene scene;

    public static SceneSerializer create(Scene scene) {
        return new SceneSerializer(scene);
    }

    private SceneSerializer(Scene scene) {
        this.scene = scene;
    }

    public void serialize(String filePath) {
        Yaml yaml = new Yaml();
        yaml.dump(serializeScene(), Assets.getFileWriter(filePath));
    }

    private Map<String, Object> serializeScene() {
        Map<String, Object> serializedScene = new HashMap<>();
        serializedScene.put("name", "Scene name");
        serializedScene.put("entities", serializeEntities());
        return serializedScene;
    }

    private List<Map<String, Object>> serializeEntities() {
        return scene.getRegistry().stream()
            .map(entity -> serializeEntity(new Entity(entity, scene)))
            .toList();
    }

    private Map<String, Object> serializeEntity(Entity entity) {
        Map<String, Object> serializedEntity = new HashMap<>();

        serializedEntity.put("id", entity.get());
        serializedEntity.put("components", serializeComponents(entity));
        return serializedEntity;
    }

    private List<Map<String, Object>> serializeComponents(Entity entity) {
        List<Map<String, Object>> components = new ArrayList<>();

        if (entity.hasComponent(TagComponent.class))
            components.add(serializeTagComponent(entity.getComponent(TagComponent.class)));

        if (entity.hasComponent(TransformComponent.class))
            components.add(serializeTransformComponent(entity.getComponent(TransformComponent.class)));

        if (entity.hasComponent(SpriteRendererComponent.class))
            components.add(serializeSpriteRendererComponent(entity.getComponent(SpriteRendererComponent.class)));

        if (entity.hasComponent(CameraComponent.class))
            components.add(serializeCameraComponent(entity.getComponent(CameraComponent.class)));

        return components;
    }

    private Map<String, Object> serializeTagComponent(TagComponent component) {
        Map<String, Object> serializedComponent = new HashMap<>();
        serializedComponent.put("type", "Tag Component");
        serializedComponent.put("tag", component.tag);
        return serializedComponent;
    }

    private Map<String, Object> serializeTransformComponent(TransformComponent component) {
        Map<String, Object> serializedComponent = new HashMap<>();
        serializedComponent.put("type", "Transform Component");

        Map<String, Object> serializedPosition = new HashMap<>();
        serializedPosition.put("x", component.position.x);
        serializedPosition.put("y", component.position.y);
        serializedPosition.put("z", component.position.z);
        serializedComponent.put("position", serializedPosition);

        Map<String, Object> serializedRotation = new HashMap<>();
        serializedRotation.put("x", component.rotation.x);
        serializedRotation.put("y", component.rotation.y);
        serializedRotation.put("z", component.rotation.z);
        serializedComponent.put("rotation", serializedRotation);

        Map<String, Object> serializedScale = new HashMap<>();
        serializedScale.put("x", component.scale.x);
        serializedScale.put("y", component.scale.y);
        serializedScale.put("z", component.scale.z);
        serializedComponent.put("scale", serializedScale);

        return serializedComponent;
    }

    private Map<String, Object> serializeSpriteRendererComponent(SpriteRendererComponent component) {
        Map<String, Object> serializedComponent = new HashMap<>();
        serializedComponent.put("type", "SpriteRenderer Component");

        Map<String, Object> serializedColor = new HashMap<>();
        serializedColor.put("r", component.color.r);
        serializedColor.put("g", component.color.g);
        serializedColor.put("b", component.color.b);
        serializedColor.put("a", component.color.a);
        serializedComponent.put("color", serializedColor);

        return serializedComponent;
    }

    private Map<String, Object> serializeCameraComponent(CameraComponent component) {
        Map<String, Object> serializedComponent = new HashMap<>();
        serializedComponent.put("type", "Camera Component");
        serializedComponent.put("isPrimary", component.isPrimary);
        serializedComponent.put("hasFixedAspectRatio", component.hasFixedAspectRatio);

        Map<String, Object> serializedCamera = new HashMap<>();
        serializedCamera.put("perspectiveFOV", component.camera.getPerspectiveFOV());
        serializedCamera.put("perspectiveNearClip", component.camera.getPerspectiveNearClip());
        serializedCamera.put("perspectiveFarClip", component.camera.getPerspectiveFarClip());
        serializedCamera.put("orthographicSize", component.camera.getOrthographicSize());
        serializedCamera.put("orthographicNearClip", component.camera.getOrthographicNearClip());
        serializedCamera.put("orthographicFarClip", component.camera.getOrthographicFarClip());
        serializedComponent.put("camera", serializedCamera);

        return serializedComponent;
    }
}
