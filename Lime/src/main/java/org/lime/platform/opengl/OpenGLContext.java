package org.lime.platform.opengl;

import org.lime.core.renderer.GraphicsContext;

import static org.lime.core.utils.Log.LM_CORE_INFO;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL46.*;

public class OpenGLContext extends GraphicsContext {

    private long windowHandle;

    public OpenGLContext(long windowHandle) {
        this.windowHandle = windowHandle;
    }

    @Override
    public void init() {
        glfwMakeContextCurrent(windowHandle);
        createCapabilities();

        LM_CORE_INFO("OpenGL Info:");
        LM_CORE_INFO(String.format("  Vendor: %s", glGetString(GL_VENDOR)));
        LM_CORE_INFO(String.format("  Renderer: %s", glGetString(GL_RENDERER)));
        LM_CORE_INFO(String.format("  Version: %s", glGetString(GL_VERSION)));
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }
}
