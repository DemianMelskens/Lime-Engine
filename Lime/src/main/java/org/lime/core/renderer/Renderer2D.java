package org.lime.core.renderer;

import org.joml.Matrix4f;
import org.lime.core.renderer.camera.OrthographicCamera;

public class Renderer2D {
    private static Matrix4f viewProjectionMatrix;

    public static RendererAPI.Type getAPI() {
        return RendererAPI.getType();
    }

    public static void init() {
        RenderCommand.init();
    }

    public static void beginScene(OrthographicCamera camera) {
        Renderer2D.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }

    public static void endScene() {
    }

    public static void drawQuad() {

    }
}
