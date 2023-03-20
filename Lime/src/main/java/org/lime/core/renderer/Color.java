package org.lime.core.renderer;

import lombok.Getter;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class Color {
    @Getter
    private Vector4f value;

    public static Color white() {
        return new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static Color red() {
        return new Color(0.8f, 0.2f, 0.3f, 1.0f);
    }

    public static Color green() {
        return new Color(0.3f, 0.8f, 0.2f, 1.0f);
    }

    public static Color blue() {
        return new Color(0.2f, 0.3f, 0.8f, 1.0f);
    }

    public static Color create(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    public static Color create(String hex) {
        return new Color(hex);
    }

    private Color(float r, float g, float b, float a) {
        this.value = new Vector4f(r, g, b, a);
    }

    private Color(String hex) {
        this.value = new Vector4f(
                toColorSpace(hex.substring(1, 3)),
                toColorSpace(hex.substring(3, 5)),
                toColorSpace(hex.substring(5, 7)),
                getOptionalAlpha(hex)
        );
    }

    public void set(float r, float g, float b, float a) {
        value.set(r, g, b, a);
    }

    public float r() {
        return value.x;
    }

    public void r(float r) {
        value.x = r;
    }

    public float g() {
        return value.y;
    }

    public void g(float g) {
        value.y = g;
    }

    public float b() {
        return value.z;
    }

    public void b(float b) {
        value.z = b;
    }

    public float a() {
        return value.w;
    }

    public void a(float a) {
        value.w = a;
    }

    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        buffer.put((byte) (255.0f * r()));
        buffer.put((byte) (255.0f * g()));
        buffer.put((byte) (255.0f * b()));
        buffer.put((byte) (255.0f * a()));
        buffer.rewind();
        return buffer;
    }

    private float getOptionalAlpha(String hex) {
        try {
            return toColorSpace(hex.substring(7, 9));
        } catch (IndexOutOfBoundsException ignored) {
            return 1.0f;
        }
    }

    /***
     * puts a hex 0-255 space to 0-1 space used by engine
     * @param value
     * @return normalized color value
     */
    private float toColorSpace(String value) {
        return Integer.valueOf(value, 16).floatValue() / 255;
    }
}
