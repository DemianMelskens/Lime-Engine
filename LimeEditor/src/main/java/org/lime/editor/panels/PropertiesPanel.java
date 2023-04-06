package org.lime.editor.panels;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.lime.core.scene.Entity;
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

//        if (squareEntity.isValid()) {
//            ImGui.separator();
//            ImGui.text(squareEntity.getComponent(TagComponent.class).tag);
//
//            Color color = squareEntity.getComponent(SpriteRendererComponent.class).color;
//            float[] colorValue = new float[]{color.r(), color.g(), color.b(), color.a()};
//            if (ImGui.colorEdit4("Color", colorValue)) {
//                color.set(colorValue[0], colorValue[1], colorValue[2], colorValue[3]);
//            }
//            ImGui.separator();
//        }
//
//        if (cameraEntity.isValid()) {
//            Matrix4f transform = cameraEntity.getComponent(TransformComponent.class).transform;
//            float[] transformValue = new float[]{transform.m30(), transform.m31(), transform.m32()};
//            if (ImGui.dragFloat3("Camera Transform", transformValue)) {
//                transform.m30(transformValue[0]);
//                transform.m31(transformValue[1]);
//                transform.m32(transformValue[2]);
//            }
//
//            if (ImGui.checkbox("Camera A", primaryCamera)) {
//                primaryCamera = !primaryCamera;
//                cameraEntity.getComponent(CameraComponent.class).isPrimary = primaryCamera;
//                secondCamera.getComponent(CameraComponent.class).isPrimary = !primaryCamera;
//            }
//        }
//
//        var camera = secondCamera.getComponent(CameraComponent.class).camera;
//        float[] orthoSize = new float[]{camera.getOrthographicSize()};
//        if (ImGui.dragFloat("Second camera ortho size", orthoSize)) {
//            camera.setOrthographicSize(orthoSize[0]);
//        }

        ImGui.end();
    }

    private void drawComponents(Entity context) {
        if (context.hasComponent(TagComponent.class))
            drawTagComponent(context);
        if (context.hasComponent(TransformComponent.class))
            drawTransformComponent(context);
    }

    private void drawTagComponent(Entity context) {
        var tagComponent = context.getComponent(TagComponent.class);
        ImString output = new ImString(tagComponent.tag, 256);
        if (ImGui.inputText("Tag", output)) {
            tagComponent.tag = output.get();
        }
    }

    private void drawTransformComponent(Entity context) {
        var transformComponent = context.getComponent(TransformComponent.class);

        int flags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.SpanAvailWidth;
        flags |= ImGuiTreeNodeFlags.OpenOnDoubleClick;
        if (ImGui.treeNodeEx(transformComponent.hashCode(), flags, "Transform")) {
            float[] positionOutput = new float[]{transformComponent.transform.m30(), transformComponent.transform.m31(), transformComponent.transform.m32()};
            if (ImGui.dragFloat3("Position", positionOutput)) {
                transformComponent.transform.m30(positionOutput[0])
                        .m31(positionOutput[1])
                        .m32(positionOutput[2]);
            }
            ImGui.treePop();
        }
    }
}
