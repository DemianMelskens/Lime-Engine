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

    private Color(float r, float g, float b, float a) {
        this.value = new Vector4f(r, g, b, a);
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
}
