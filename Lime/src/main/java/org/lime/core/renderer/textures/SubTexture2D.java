package org.lime.core.renderer.textures;

import lombok.Getter;
import org.joml.Vector2f;

@Getter
public class SubTexture2D {

    private Texture2D texture;
    private Vector2f[] textureCoordinates = new Vector2f[4];

    public static SubTexture2D create(Texture2D texture, Vector2f coordinates, Vector2f spriteSize) {
        Vector2f min = new Vector2f((coordinates.x * spriteSize.x) / texture.getWidth(), (coordinates.y * spriteSize.y) / texture.getHeight());
        Vector2f max = new Vector2f(((coordinates.x + 1.0f) * spriteSize.x) / texture.getWidth(), ((coordinates.y + 1.0f) * spriteSize.y) / texture.getHeight());
        return new SubTexture2D(texture, min, max);
    }

    private SubTexture2D(Texture2D texture, Vector2f min, Vector2f max) {
        this.texture = texture;
        this.textureCoordinates[0] = new Vector2f(min.x, min.y);
        this.textureCoordinates[1] = new Vector2f(max.x, min.y);
        this.textureCoordinates[2] = new Vector2f(max.x, max.y);
        this.textureCoordinates[3] = new Vector2f(min.x, max.y);
    }
}
