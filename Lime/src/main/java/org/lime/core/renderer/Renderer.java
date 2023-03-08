package org.lime.core.renderer;

import org.joml.Matrix4f;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.platform.opengl.renderer.OpenGLShader;

public class Renderer {

    private static OrthographicCamera camera;

    public static RendererAPI.Type getAPI() {
        return RendererAPI.getType();
    }

    public static void beginScene(OrthographicCamera camera) {
        Renderer.camera = camera;
    }

    public static void endScene() {

    }

    public static void submit(Shader shader, VertexArray vertexArray) {
        submit(shader, vertexArray, new Matrix4f());
    }

    public static void submit(Shader shader, VertexArray vertexArray, Matrix4f transform) {
        shader.bind();
        ((OpenGLShader) shader).uploadUniformMat4("u_ViewProjection", camera.getViewProjectionMatrix());
        ((OpenGLShader) shader).uploadUniformMat4("u_Transform", transform);

        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
}
