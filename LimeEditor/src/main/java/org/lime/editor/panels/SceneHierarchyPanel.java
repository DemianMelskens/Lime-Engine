package org.lime.editor.panels;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.TagComponent;

import static org.lime.core.utils.Log.LM_INFO;

public class SceneHierarchyPanel {
    private final PropertiesPanel propertiesPanel;
    private Scene context;
    private Entity selectionContext;

    public SceneHierarchyPanel() {
        this.propertiesPanel = new PropertiesPanel();
    }

    public void setContext(Scene scene) {
        this.context = scene;
    }

    public void onImGuiRender() {
        ImGui.begin("Scene Hierarchy");

        context.getRegistry()
                .forEach(entity -> drawEntityNode(new Entity(entity, context)));

        if (ImGui.isMouseDown(0) && ImGui.isWindowHovered()) {
            this.setSelection(null);
        }

        ImGui.end();

        propertiesPanel.onImGuiRender();
    }

    private void drawEntityNode(Entity entity) {
        String tag = entity.getComponent(TagComponent.class).tag;

        int flags = (entity.equals(selectionContext) ? ImGuiTreeNodeFlags.Selected : 0);
        flags |= ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.OpenOnDoubleClick;
        boolean opened = ImGui.treeNodeEx(entity.getEntityHandle().toString(), flags, tag);
        if (ImGui.isItemClicked()) {
            this.setSelection(entity);
        }

        if (opened)
            ImGui.treePop();
    }

    private void setSelection(Entity entity) {
        this.selectionContext = entity;
        propertiesPanel.setContext(entity);
    }
}
