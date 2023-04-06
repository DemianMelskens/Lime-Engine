package org.lime.editor.panels;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.joml.Math;
import org.joml.Vector3f;
import org.lime.core.imgui.ImGuiControls;
import org.lime.core.renderer.camera.ProjectionType;
import org.lime.core.scene.Entity;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.TagComponent;
import org.lime.core.scene.components.TransformComponent;

public class PropertiesPanel {
    private Entity context;

    public void setContext(Entity context) {
        this.context = context;
    }

    public void onImGuiRender() {
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
    }

    private void drawTagComponent(Entity context) {
        var tagComponent = context.getComponent(TagComponent.class);
        ImGuiControls.inputText("Tag", tagComponent.tag, output ->
                tagComponent.tag = output
        );
    }

    private void drawTransformComponent(Entity context) {

        int flags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.SpanAvailWidth;
        flags |= ImGuiTreeNodeFlags.OpenOnDoubleClick;
        if (ImGui.treeNodeEx(TransformComponent.class.hashCode(), flags, "Transform")) {
            var transform = context.getComponent(TransformComponent.class).transform;
            ImGuiControls.dragFloat3("Position", new Vector3f(transform.m30(), transform.m31(), transform.m32()), output ->
                    transform.m30(output.x)
                            .m31(output.y)
                            .m32(output.z)
            );
            ImGui.treePop();
        }
    }

    private void drawCameraComponent(Entity context) {
        int flags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.SpanAvailWidth;
        flags |= ImGuiTreeNodeFlags.OpenOnDoubleClick;
        if (ImGui.treeNodeEx(CameraComponent.class.hashCode(), flags, "Camera")) {
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

            ImGui.treePop();
        }
    }
}
