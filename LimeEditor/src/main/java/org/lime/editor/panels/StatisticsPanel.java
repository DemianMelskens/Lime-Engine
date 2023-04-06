package org.lime.editor.panels;

import imgui.ImGui;
import org.lime.core.renderer.Renderer2D;

public class StatisticsPanel {

    public void onImGuiRender() {
        ImGui.begin("Statistics");

        ImGui.text(String.format("%d drawCalls", Renderer2D.getStatistics().drawCalls));
        ImGui.text(String.format("%d quad Count", Renderer2D.getStatistics().quadCount));
        ImGui.text(String.format("%d vertex Count", Renderer2D.getStatistics().getTotalVertexCount()));
        ImGui.text(String.format("%d index Count", Renderer2D.getStatistics().getTotalIndexCount()));

        ImGui.end();
    }
}
