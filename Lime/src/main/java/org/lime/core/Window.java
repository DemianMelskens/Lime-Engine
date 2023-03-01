package org.lime.core;

import lombok.Getter;
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
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.system.MemoryUtil;

import java.util.function.Consumer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;
import static org.lime.core.utils.Log.LM_CORE_ERROR;
import static org.lime.core.utils.Log.LM_CORE_INFO;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

@Getter
public class Window {
    private static boolean GLFW_INITIALIZED = false;
    private static GLFWErrorCallbackI GLFW_ERROR_CALLBACK = (error, description) -> {
        LM_CORE_ERROR(String.format("GLFW Error (%s): %s", error, description));
    };

    private String title;
    private int width;
    private int height;
    private boolean vSync;
    private Consumer<Event> eventCallback;
    private long windowHandle;

    public static Window create() {
        return create("Lime Engine", 1280, 720);
    }

    public static Window create(String title, int width, int height) {
        return new Window(title, width, height);
    }

    private Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        setUp();
    }

    public void setVSync(boolean enabled) {
        glfwSwapInterval(enabled ? 1 : 0);
        vSync = enabled;
    }

    public void setEventCallback(Consumer<Event> eventCallback) {
        this.eventCallback = eventCallback;
    }

    private void setUp() {
        LM_CORE_INFO(String.format("Creating window %s (%d, %d)", title, width, height));

        if (!GLFW_INITIALIZED) {
            LM_CORE_ASSERT(glfwInit(), "Could not initialize GLFW!");
            glfwSetErrorCallback(GLFW_ERROR_CALLBACK);
            GLFW_INITIALIZED = true;
        }

        windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        glfwMakeContextCurrent(windowHandle);
        createCapabilities();
        setVSync(true);

        // set GLFW callbacks
        glfwSetWindowSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;

            eventCallback.accept(new WindowResizeEvent(width, height));
        });

        glfwSetWindowCloseCallback(windowHandle, w -> {
            tearDown();
            eventCallback.accept(new WindowCloseEvent());
        });

        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS -> eventCallback.accept(new KeyPressedEvent(key, 0));
                case GLFW_RELEASE -> eventCallback.accept(new KeyReleasedEvent(key));
                case GLFW_REPEAT -> eventCallback.accept(new KeyPressedEvent(key, 1));
            }
        });

        glfwSetCharCallback(windowHandle, (w, character) -> eventCallback.accept(new KeyTypedEvent(character)));

        glfwSetMouseButtonCallback(windowHandle, (w, button, action, mods) -> {
            switch (action) {
                case GLFW_PRESS -> eventCallback.accept(new MouseButtonPressedEvent(button));
                case GLFW_RELEASE -> eventCallback.accept(new MouseButtonReleasedEvent(button));
            }
        });

        glfwSetScrollCallback(
                windowHandle,
                (w, xoffset, yoffset) -> eventCallback.accept(new MouseScrollEvent((float) xoffset, (float) yoffset))
        );

        glfwSetCursorPosCallback(
                windowHandle,
                (w, xpos, ypos) -> eventCallback.accept(new MouseMoveEvent((float) xpos, (float) ypos))
        );
    }

    public void onUpdate() {
        glfwPollEvents();
        glfwSwapBuffers(windowHandle);
    }

    private void tearDown() {
        glfwDestroyWindow(windowHandle);
    }
}
