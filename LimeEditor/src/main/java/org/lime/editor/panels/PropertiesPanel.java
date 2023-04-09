package org.lime.editor.panels;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.joml.Math;
import org.joml.Vector3f;
import org.lime.core.imgui.ImGuiControls;
import org.lime.core.renderer.camera.ProjectionType;
import org.lime.core.scene.Entity;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TagComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.utils.VectorMath;

public class PropertiesPanel {
    private Entity context;

    public void setContext(Entity context) {
        this.context = context;
    }

    public void onImGuiRender() {
        ImGui.showDemoWindow();
        ImGui.begin("Properties");

        if (context != null) {
            drawComponents(context);
        }

        ImGui.end();
    }

    private void drawComponents(Entity context) {
        if (context.hasComponent(TagComponent.class))
            drawTagComponent(context);

        if (context.hasComponent(TransformComponent.class))
            drawTransformComponent(context);

        if (context.hasComponent(CameraComponent.class))
            drawCameraComponent(context);

        if (context.hasComponent(SpriteRendererComponent.class))
            drawSpriteRendererComponent(context);
    }

    private void drawTagComponent(Entity context) {
        var tagComponent = context.getComponent(TagComponent.class);
        ImGuiControls.inputText("Tag", tagComponent.tag, output ->
                tagComponent.tag = output
        );
    }

    private void drawTransformComponent(Entity context) {
        drawComponent(TransformComponent.class, "Transform", () -> {
            var transformComponent = context.getComponent(TransformComponent.class);

            var position = transformComponent.position;
            ImGuiControls.dragFloat3("Position", position, output -> {
                position.x = output.x;
                position.y = output.y;
                position.z = output.z;
            });

            var rotation = transformComponent.rotation;
            ImGuiControls.dragFloat3("Rotation", VectorMath.toDegrees(rotation), output -> {
                transformComponent.rotation = VectorMath.toRadians(output);
            });

            var scale = transformComponent.scale;
            ImGuiControls.dragFloat3("Scale", scale, output -> {
                scale.x = output.x;
                scale.y = output.y;
                scale.z = output.z;
            });
        });
    }

    private void drawCameraComponent(Entity context) {
        drawComponent(CameraComponent.class, "Camera", () -> {
            var cameraComponent = context.getComponent(CameraComponent.class);
            var camera = cameraComponent.camera;

            ImGuiControls.checkbox("Primary", cameraComponent.isPrimary, output ->
                    cameraComponent.isPrimary = output
            );

            ProjectionType currentProjectionType = camera.getProjectionType();
            if (ImGui.beginCombo("Projection", currentProjectionType.name())) {
                for (ProjectionType type : ProjectionType.values()) {
                    boolean isSelected = currentProjectionType.equals(type);

                    if (ImGui.selectable(type.name(), isSelected)) {
                        camera.setProjectionType(type);
                    }

                    if (isSelected)
                        ImGui.setItemDefaultFocus();

                }
                ImGui.endCombo();
            }

            if (ProjectionType.Perspective.equals(currentProjectionType)) {
                float perspectiveFOV = camera.getPerspectiveFOV();
                ImGuiControls.dragFloat("FOV", (float) Math.toDegrees(perspectiveFOV), output ->
                        camera.setPerspectiveFOV(Math.toRadians(output))
                );

                float perspectiveNearClip = camera.getPerspectiveNearClip();
                ImGuiControls.dragFloat("Near", perspectiveNearClip, camera::setPerspectiveNearClip);

                float perspectiveFarClip = camera.getPerspectiveFarClip();
                ImGuiControls.dragFloat("Far", perspectiveFarClip, camera::setPerspectiveFarClip);
            }

            if (ProjectionType.Orthographic.equals(currentProjectionType)) {
                float orthographicSize = camera.getOrthographicSize();
                ImGuiControls.dragFloat("Size", orthographicSize, camera::setOrthographicSize);

                float orthographicNearClip = camera.getOrthographicNearClip();
                ImGuiControls.dragFloat("Near", orthographicNearClip, camera::setOrthographicNearClip);

                float orthographicFarClip = camera.getOrthographicFarClip();
                ImGuiControls.dragFloat("Far", orthographicFarClip, camera::setOrthographicFarClip);
            }
        });
    }

    private void drawSpriteRendererComponent(Entity context) {
        drawComponent(SpriteRendererComponent.class, "Sprite Renderer", () -> {
            var spriteRendererComponent = context.getComponent(SpriteRendererComponent.class);
            ImGuiControls.colorEdit4("Color", spriteRendererComponent.color, (output) ->
                    spriteRendererComponent.color = output
            );
        });
    }

    private void drawComponent(Class<?> clazz, String label, Runnable children) {
        int flags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.SpanAvailWidth;
        flags |= ImGuiTreeNodeFlags.OpenOnDoubleClick;
        if (ImGui.treeNodeEx(clazz.hashCode(), flags, label)) {
            children.run();
            ImGui.treePop();
        }
    }
}
