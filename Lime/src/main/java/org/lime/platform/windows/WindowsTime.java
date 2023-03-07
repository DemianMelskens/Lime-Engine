package org.lime.platform.windows;

import org.lime.core.time.Time;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class WindowsTime extends Time {

    @Override
    public float internalGetTime() {
        return (float) glfwGetTime();
    }
}
