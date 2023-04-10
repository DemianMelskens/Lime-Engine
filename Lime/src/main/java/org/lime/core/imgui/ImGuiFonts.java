package org.lime.core.imgui;

import imgui.*;
import org.lime.core.utils.Resources;

import java.util.HashMap;
import java.util.Map;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class ImGuiFonts {

    private static ImGuiFonts instance;

    private Map<String, ImFont> loadedFonts;

    private ImGuiFonts() {
        this.loadedFonts = new HashMap<>();
    }

    public synchronized static ImGuiFonts get() {
        if (instance == null)
            instance = new ImGuiFonts();

        return instance;
    }

    public void initFonts(ImGuiIO io) {
        ImFontAtlas fontAtlas = io.getFonts();

        addFontWithIcons(io, fontAtlas, 12.0f,
            () -> addFont(fontAtlas, "OpenSans-Regular", "/fonts/open-sans/OpenSans-Regular.ttf", 18.0f)
        );
        addFontWithIcons(io, fontAtlas, 12.0f,
            () -> addFont(fontAtlas, "OpenSans-Bold", "/fonts/open-sans/OpenSans-Bold.ttf", 18.0f)
        );
    }

    public ImFont getFont(String name) {
        ImFont font = loadedFonts.get(name);
        LM_CORE_ASSERT(font != null, "Requested font is not loaded!");
        return font;
    }

    private void addFontWithIcons(ImGuiIO io, ImFontAtlas fontAtlas, float size, Runnable addBaseFont) {
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(ImGuiIcons._IconRange);

        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(true);

        final short[] glyphRanges = rangesBuilder.buildRanges();
        addBaseFont.run();
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/fontawesome/fa-regular-400.ttf"), size, fontConfig, glyphRanges);
        fontAtlas.addFontFromMemoryTTF(Resources.load("/fonts/fontawesome/fa-solid-900.ttf"), size, fontConfig, glyphRanges);
        io.getFonts().build();

        fontConfig.destroy();
    }

    private void addFont(ImFontAtlas fontAtlas, String name, String filePath, float size) {
        ImFont font = fontAtlas.addFontFromMemoryTTF(Resources.load(filePath), size);
        loadedFonts.put(name, font);
    }
}
