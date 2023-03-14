package org.lime.core.renderer;

import org.joml.Matrix4f;
import org.lime.core.events.application.WindowResizeEvent;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.renderer.shader.Shader;
import org.lime.platform.opengl.renderer.OpenGLShader;

public class Renderer {

    private static Matrix4f viewProjectionMatrix;

    public static RendererAPI.Type getAPI() {
        return RendererAPI.getType();
    }

    public static void init() {
        RenderCommand.init();
    }

    public static void onWindowResizedEvent(WindowResizeEvent event) {
        RenderCommand.setViewport(0, 0, event.getWidth(), event.getHeight());
    }

    public static void beginScene(OrthographicCamera camera) {
        Renderer.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }

    public static void endScene() {
    }

    public static void submit(Shader shader, VertexArray vertexArray) {
        submit(shader, vertexArray, new Matrix4f());
    }

    public static void submit(Shader shader, VertexArray vertexArray, Matrix4f transform) {
        shader.bind();
        ((OpenGLShader) shader).uploadUniformMat4("u_ViewProjection", viewProjectionMatrix);
        ((OpenGLShader) shader).uploadUniformMat4("u_Transform", transform);

        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
}
