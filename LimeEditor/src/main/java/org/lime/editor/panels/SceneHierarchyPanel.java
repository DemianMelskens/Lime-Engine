package org.lime.editor.panels;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import org.lime.core.imgui.ImGuiIcons;
import org.lime.core.scene.Entity;
import org.lime.core.scene.Scene;
import org.lime.core.scene.components.TagComponent;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.lime.core.imgui.ImGuiControls.*;

public class SceneHierarchyPanel {
    private final PropertiesPanel propertiesPanel;
    private Scene context;
    private Entity selectionContext;

    public SceneHierarchyPanel() {
        this.propertiesPanel = new PropertiesPanel();
    }

    public Entity getSelectedEntity() {
        return selectionContext;
    }

    public void setContext(Scene scene) {
        this.context = scene;
        this.selectionContext = null;
        propertiesPanel.setContext(null);
    }

    public void onImGuiRender() {
        int windowFlags = ImGuiWindowFlags.MenuBar;
        ImGui.begin("Scene Hierarchy", windowFlags);

        menuBar(
            () -> menu(
                String.format("%s %s", ImGuiIcons.Plus, ImGuiIcons.CaretDown),
                () -> menuItem("Add entity",
                    () -> context.createEntity()
                )
            )
        );

        popupContextWindow(
            () -> menuItem(String.format("%s Add entity", ImGuiIcons.Plus),
                () -> context.createEntity()
            )
        );

        Set<Entity> toDelete = context.getRegistry().stream()
            .map(entity -> new Entity(entity, context))
            .map(this::drawEntityNode)
            .collect(Collectors.toSet());

        toDelete.stream()
            .filter(Objects::nonNull)
            .forEach(context::deleteEntity);

        if (ImGui.isMouseDown(0) && ImGui.isWindowHovered()) {
            this.setSelection(null);
        }

        ImGui.end();
        propertiesPanel.onImGuiRender();
    }

    private Entity drawEntityNode(Entity entity) {
        AtomicBoolean shouldDelete = new AtomicBoolean(false);
        String tag = entity.getComponent(TagComponent.class).tag;

        int flags = (entity.equals(selectionContext) ? ImGuiTreeNodeFlags.Selected : 0);
        flags |= ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.OpenOnDoubleClick;
        boolean opened = ImGui.treeNodeEx(entity.hashCode(), flags, tag);
        if (ImGui.isItemClicked()) {
            this.setSelection(entity);
        }

        popupContextItem(() ->
            menuItem(String.format("%s Delete", ImGuiIcons.Trash),
                () -> shouldDelete.set(true)
            )
        );

        if (opened)
            ImGui.treePop();

        if (shouldDelete.get())
            return entity;
        return null;
    }

    private void setSelection(Entity entity) {
        this.selectionContext = entity;
        propertiesPanel.setContext(entity);
    }
}
