package org.lime.sandbox2d;

import org.joml.Vector3f;
import org.lime.core.Input;
import org.lime.core.Layer;
import org.lime.core.events.Event;
import org.lime.core.renderer.RenderCommand;
import org.lime.core.renderer.Renderer;
import org.lime.core.renderer.VertexArray;
import org.lime.core.renderer.buffers.BufferElement;
import org.lime.core.renderer.buffers.BufferLayout;
import org.lime.core.renderer.buffers.IndexBuffer;
import org.lime.core.renderer.buffers.VertexBuffer;
import org.lime.core.renderer.camera.OrthographicCamera;
import org.lime.core.renderer.shader.ShaderDataType;
import org.lime.core.renderer.shader.ShaderLibrary;
import org.lime.core.renderer.textures.Texture;
import org.lime.core.renderer.textures.Texture2D;
import org.lime.core.time.TimeStep;
import org.lime.platform.opengl.renderer.OpenGLShader;

import static org.lime.core.KeyCodes.*;

public class ExampleLayer extends Layer {

    public ExampleLayer() {
        super("Example");
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void onUpdate(TimeStep timestep) {
    }

    @Override
    public void onImGuiRender() {
    }

    @Override
    public void onEvent(Event event) {
    }
}
