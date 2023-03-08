package org.lime.core;

import lombok.Getter;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

@Getter
public class Image {

    ByteBuffer data;
    private int width;
    private int height;
    private int channels;

    public Image(String path) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            this.data = stbi_load(path, width, height, channels, 0);

            LM_CORE_ASSERT(data != null, String.format("Failed to load image! reason: %s", stbi_failure_reason()));

            this.width = width.get();
            this.height = height.get();
            this.channels = channels.get();
        }
    }

    public void free() {
        stbi_image_free(data);
    }
}
