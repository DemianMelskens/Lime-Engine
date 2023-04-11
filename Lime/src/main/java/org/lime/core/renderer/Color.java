package org.lime.core.renderer;

import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class Color {

    public float r;
    public float g;
    public float b;
    public float a;

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
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    private Color(String hex) {
        this.r = toColorSpace(hex.substring(1, 3));
        this.g = toColorSpace(hex.substring(3, 5));
        this.b = toColorSpace(hex.substring(5, 7));
        this.a = getOptionalAlpha(hex);
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Vector4f get() {
        return new Vector4f(r, g, b, a);
    }

    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        buffer.put((byte) (255.0f * r));
        buffer.put((byte) (255.0f * g));
        buffer.put((byte) (255.0f * b));
        buffer.put((byte) (255.0f * a));
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
