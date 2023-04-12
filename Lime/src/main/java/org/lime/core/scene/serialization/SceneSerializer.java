package org.lime.core.scene.serialization;

import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.*;
import org.lime.core.serialization.YamlWriter;

import java.io.IOException;

import static org.lime.core.utils.Assert.LM_CORE_EXCEPTION;

public class SceneSerializer {

    private Scene scene;

    public SceneSerializer(Scene scene) {
        this.scene = scene;
    }

    public void serialize(String filePath) {
        try (YamlWriter yamlWriter = new YamlWriter(filePath);) {
            handleScene(yamlWriter);
        } catch (Exception e) {
            throw LM_CORE_EXCEPTION(String.format("Failed to serialize scene. reason: %s", e.getMessage()));
        }
    }

    private void handleScene(YamlWriter yamlWriter) throws IOException {
        yamlWriter.addField("name", "Scene name");

        yamlWriter.pushList("entities");

        for (int entity : scene.getRegistry()) {
            yamlWriter.pushListItemObject("entity");
            handleEntity(yamlWriter, new Entity(entity, scene));
            yamlWriter.popListItemObject();
        }

        yamlWriter.popList();
    }

    private void handleEntity(YamlWriter yamlWriter, Entity entity) throws IOException {
        String tag = entity.getComponent(TagComponent.class).tag;
        yamlWriter.addField("tag", tag);
        yamlWriter.pushList("components");

        if (entity.hasComponent(TagComponent.class))
            handleTagComponent(yamlWriter, entity.getComponent(TagComponent.class));

        if (entity.hasComponent(TransformComponent.class))
            handleTransformComponent(yamlWriter, entity.getComponent(TransformComponent.class));

        if (entity.hasComponent(SpriteRendererComponent.class))
            handleSpriteRendererComponent(yamlWriter, entity.getComponent(SpriteRendererComponent.class));

        if (entity.hasComponent(CameraComponent.class))
            handleCameraComponent(yamlWriter, entity.getComponent(CameraComponent.class));

        if (entity.hasComponent(NativeScriptComponent.class))
            handleNativeScriptComponent(yamlWriter, entity.getComponent(NativeScriptComponent.class));

        yamlWriter.popList();
    }

    private void handleTagComponent(YamlWriter yamlWriter, TagComponent component) throws IOException {
        yamlWriter.pushListItemObject("Tag Component");
        yamlWriter.addField("tag", component.tag);
        yamlWriter.popListItemObject();
    }

    private void handleTransformComponent(YamlWriter yamlWriter, TransformComponent component) throws IOException {
        yamlWriter.pushListItemObject("Transform Component");

        yamlWriter.pushObject("position");
        yamlWriter.addField("x", component.position.x);
        yamlWriter.addField("y", component.position.y);
        yamlWriter.addField("z", component.position.z);
        yamlWriter.popObject();

        yamlWriter.pushObject("rotation");
        yamlWriter.addField("x", component.rotation.x);
        yamlWriter.addField("y", component.rotation.y);
        yamlWriter.addField("z", component.rotation.z);
        yamlWriter.popObject();

        yamlWriter.pushObject("scale");
        yamlWriter.addField("x", component.scale.x);
        yamlWriter.addField("y", component.scale.y);
        yamlWriter.addField("z", component.scale.z);
        yamlWriter.popObject();

        yamlWriter.popListItemObject();
    }

    private void handleSpriteRendererComponent(YamlWriter yamlWriter, SpriteRendererComponent component) throws IOException {
        yamlWriter.pushListItemObject("SpriteRenderer Component");

        yamlWriter.pushObject("color");
        yamlWriter.addField("r", component.color.r);
        yamlWriter.addField("g", component.color.g);
        yamlWriter.addField("b", component.color.b);
        yamlWriter.addField("a", component.color.a);
        yamlWriter.popObject();

        yamlWriter.popListItemObject();
    }

    private void handleCameraComponent(YamlWriter yamlWriter, CameraComponent component) throws IOException {
        yamlWriter.pushListItemObject("Camera Component");

        yamlWriter.addField("isPrimary", component.isPrimary);
        yamlWriter.addField("hasFixedAspectRatio", component.hasFixedAspectRatio);

        yamlWriter.pushObject("camera");

        yamlWriter.addField("perspectiveFOV", component.camera.getPerspectiveFOV());
        yamlWriter.addField("perspectiveNearClip", component.camera.getPerspectiveNearClip());
        yamlWriter.addField("perspectiveFarClip", component.camera.getPerspectiveFarClip());

        yamlWriter.addField("orthographicSize", component.camera.getOrthographicSize());
        yamlWriter.addField("orthographicNearClip", component.camera.getOrthographicNearClip());
        yamlWriter.addField("orthographicFarClip", component.camera.getOrthographicFarClip());

        yamlWriter.popObject();

        yamlWriter.popListItemObject();
    }

    private void handleNativeScriptComponent(YamlWriter yamlWriter, NativeScriptComponent component) throws IOException {
//        yamlWriter.pushListItemObject("Native Script Component");
//        yamlWriter.addField("class", );
//        yamlWriter.popListItemObject();
    }
}
