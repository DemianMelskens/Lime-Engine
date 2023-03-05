package org.lime.platform.windows;

import org.lime.core.Window;
import org.lime.core.events.Event;
import org.lime.core.events.application.WindowCloseEvent;
import org.lime.core.events.application.WindowResizeEvent;
import org.lime.core.events.key.KeyPressedEvent;
import org.lime.core.events.key.KeyReleasedEvent;
import org.lime.core.events.key.KeyTypedEvent;
import org.lime.core.events.mouse.MouseButtonPressedEvent;
import org.lime.core.events.mouse.MouseButtonReleasedEvent;
import org.lime.core.events.mouse.MouseMoveEvent;
import org.lime.core.events.mouse.MouseScrollEvent;
import org.lime.platform.opengl.OpenGLContext;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.system.MemoryUtil;

import java.util.function.Consumer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Log.LM_CORE_ERROR;
import static org.lime.core.utils.Log.LM_CORE_INFO;
import static org.lwjgl.glfw.GLFW.*;

public class WindowsWindow extends Window {

    private static boolean GLFW_INITIALIZED = false;
    private static GLFWErrorCallbackI GLFW_ERROR_CALLBACK = (error, description) -> {
        LM_CORE_ERROR(String.format("GLFW Error (%s): %s", error, description));
    };

    public WindowsWindow(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    public void setVSync(boolean enabled) {
        glfwSwapInterval(enabled ? 1 : 0);
        vSync = enabled;
    }

    public void setEventCallback(Consumer<Event> eventCallback) {
        this.eventCallback = eventCallback;
    }

    @Override
    protected void init(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        LM_CORE_INFO(String.format("Creating window %s (%d, %d)", title, width, height));

        if (!GLFW_INITIALIZED) {
            LM_CORE_ASSERT(glfwInit(), "Could not initialize GLFW!");
            glfwSetErrorCallback(GLFW_ERROR_CALLBACK);
            GLFW_INITIALIZED = true;
        }

        windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        this.context = new OpenGLContext(windowHandle);
        context.init();

        setVSync(true);

        // set GLFW callbacks
        glfwSetWindowSizeCallback(windowHandle, (window, w, h) -> {
            this.width = w;
            this.height = h;

            eventCallback.accept(new WindowResizeEvent(w, h));
        });

        glfwSetWindowCloseCallback(windowHandle, window -> {
            tearDown();
            eventCallback.accept(new WindowCloseEvent());
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS -> eventCallback.accept(new KeyPressedEvent(key, 0));
                case GLFW_RELEASE -> eventCallback.accept(new KeyReleasedEvent(key));
                case GLFW_REPEAT -> eventCallback.accept(new KeyPressedEvent(key, 1));
            }
        });

        glfwSetCharCallback(windowHandle, (window, character) -> eventCallback.accept(new KeyTypedEvent(character)));

        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            switch (action) {
                case GLFW_PRESS -> eventCallback.accept(new MouseButtonPressedEvent(button));
                case GLFW_RELEASE -> eventCallback.accept(new MouseButtonReleasedEvent(button));
            }
        });

        glfwSetScrollCallback(
                windowHandle,
                (window, xoffset, yoffset) -> eventCallback.accept(new MouseScrollEvent((float) xoffset, (float) yoffset))
        );

        glfwSetCursorPosCallback(
                windowHandle,
                (window, xpos, ypos) -> eventCallback.accept(new MouseMoveEvent((float) xpos, (float) ypos))
        );
    }

    @Override
    public void onUpdate() {
        glfwPollEvents();
        context.swapBuffers();
    }

    @Override
    protected void tearDown() {
        glfwDestroyWindow(windowHandle);
    }
}
