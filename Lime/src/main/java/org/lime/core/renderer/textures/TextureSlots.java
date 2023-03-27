package org.lime.core.renderer.textures;

import java.util.ArrayList;
import java.util.List;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public class TextureSlots {
    public static final int MAX_SLOTS = 32;
    private final List<Texture2D> slots;

    public static TextureSlots create() {
        return new TextureSlots();
    }

    private TextureSlots() {
        slots = new ArrayList<>();
    }

    public int add(Texture2D texture) {
        LM_CORE_ASSERT(slots.size() <= MAX_SLOTS, "Maximum supported textures reached!");

        int index = slots.indexOf(texture);
        if (index != -1) {
            return index;
        }

        int slot = slots.size();
        slots.add(slot, texture);
        return slot;
    }

    public Texture2D get(int index) {
        return slots.get(index);
    }

    public int size() {
        return slots.size();
    }
}
