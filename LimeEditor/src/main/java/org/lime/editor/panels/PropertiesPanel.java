package org.lime.editor.panels;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import org.joml.Math;
import org.lime.core.imgui.ImGuiIcons;
import org.lime.core.imgui.ImGuiUtils;
import org.lime.core.renderer.camera.ProjectionType;
import org.lime.core.scene.Entity;
import org.lime.core.scene.components.CameraComponent;
import org.lime.core.scene.components.SpriteRendererComponent;
import org.lime.core.scene.components.TagComponent;
import org.lime.core.scene.components.TransformComponent;
import org.lime.core.utils.VectorMath;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lime.core.imgui.ImGuiControls.*;

public class PropertiesPanel {
    private Entity context;

    public void setContext(Entity context) {
        this.context = context;
    }

    public void onImGuiRender() {
        ImGui.begin("Properties");

        if (context != null && context.isValid()) {
            drawComponents(context);
        }

        ImGui.end();
    }

    private void drawComponents(Entity context) {
        if (context.hasComponent(TagComponent.class)) {
            drawTagComponent(context);
            ImGui.sameLine();
            ImGui.pushItemWidth(-1);

            button(String.format("%s Add %s", ImGuiIcons.Plus, ImGuiIcons.CaretDown),
                () -> ImGui.openPopup("AddPopup")
            );

            popup("AddPopup", () -> {
                menuItem("Camera", () -> {
                    context.addComponent(CameraComponent.class);
                    ImGui.closeCurrentPopup();
                });

                menuItem("Sprite Renderer", () -> {
                    context.addComponent(SpriteRendererComponent.class);
                    ImGui.closeCurrentPopup();
                });
            });
            ImGui.popItemWidth();
        }

        if (context.hasComponent(TransformComponent.class))
            drawTransformComponent(context);

        if (context.hasComponent(CameraComponent.class))
            drawCameraComponent(context);

        if (context.hasComponent(SpriteRendererComponent.class))
            drawSpriteRendererComponent(context);
    }

    private void drawTagComponent(Entity context) {
        var tagComponent = context.getComponent(TagComponent.class);
        inputText("##Tag", tagComponent.tag, output ->
            tagComponent.tag = output
        );
    }

    private void drawTransformComponent(Entity context) {
        drawComponent(TransformComponent.class, "Transform", () -> {
            var transformComponent = context.getComponent(TransformComponent.class);

            var position = transformComponent.position;
            dragFloat3("Position", position, output -> {
                position.x = output.x;
                position.y = output.y;
                position.z = output.z;
            });

            var rotation = transformComponent.rotation;
            dragFloat3("Rotation", VectorMath.toDegrees(rotation), output -> {
                transformComponent.rotation = VectorMath.toRadians(output);
            });

            var scale = transformComponent.scale;
            dragFloat3("Scale", scale, output -> {
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

            checkbox("Primary", cameraComponent.isPrimary, output ->
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
                dragFloat("FOV", (float) Math.toDegrees(perspectiveFOV), output ->
                    camera.setPerspectiveFOV(Math.toRadians(output))
                );

                float perspectiveNearClip = camera.getPerspectiveNearClip();
                dragFloat("Near", perspectiveNearClip, camera::setPerspectiveNearClip);

                float perspectiveFarClip = camera.getPerspectiveFarClip();
                dragFloat("Far", perspectiveFarClip, camera::setPerspectiveFarClip);
            }

            if (ProjectionType.Orthographic.equals(currentProjectionType)) {
                float orthographicSize = camera.getOrthographicSize();
                dragFloat("Size", orthographicSize, camera::setOrthographicSize);

                float orthographicNearClip = camera.getOrthographicNearClip();
                dragFloat("Near", orthographicNearClip, camera::setOrthographicNearClip);

                float orthographicFarClip = camera.getOrthographicFarClip();
                dragFloat("Far", orthographicFarClip, camera::setOrthographicFarClip);
            }
        });
    }

    private void drawSpriteRendererComponent(Entity context) {
        drawComponent(SpriteRendererComponent.class, "Sprite Renderer", () -> {
            var spriteRendererComponent = context.getComponent(SpriteRendererComponent.class);
            colorEdit4("Color", spriteRendererComponent.color, (output) ->
                spriteRendererComponent.color = output
            );
        });
    }

    private void drawComponent(Class<?> clazz, String label, Runnable children) {
        AtomicBoolean shouldRemove = new AtomicBoolean(false);
        int flags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.Framed;
        flags |= ImGuiTreeNodeFlags.OpenOnDoubleClick | ImGuiTreeNodeFlags.AllowItemOverlap | ImGuiTreeNodeFlags.FramePadding;
        ImVec2 contentRegionAvailable = ImGui.getContentRegionAvail();

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4.0f, 4.0f);
        float lineHeight = ImGuiUtils.getLineHeight();
        ImGui.separator();
        boolean open = ImGui.treeNodeEx(clazz.hashCode(), flags, label);
        ImGui.popStyleVar();

        ImGui.sameLine(contentRegionAvailable.x - (lineHeight * 0.5f));
        button(String.format("%s", ImGuiIcons.EllipsisH), lineHeight, lineHeight,
            () -> ImGui.openPopup("ComponentContext")
        );

        popup("ComponentContext",
            () -> menuItem(String.format("%s Remove", ImGuiIcons.Trash),
                () -> {
                    shouldRemove.set(true);
                    ImGui.closeCurrentPopup();
                }
            )
        );

        if (open) {
            children.run();
            ImGui.treePop();
        }

        if (shouldRemove.get())
            context.removeComponent(clazz);
    }
}
