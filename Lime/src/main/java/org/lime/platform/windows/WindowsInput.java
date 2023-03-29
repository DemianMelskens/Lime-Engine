package org.lime.platform.windows;

import org.lime.core.Application;
import org.lime.core.Input;
import org.lime.core.utils.Tuple;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class WindowsInput extends Input {
    @Override
    protected boolean internalIsKeyPressed(int keycode) {
        long window = Application.getWindow().getWindowHandle();
        int state = GLFW.glfwGetKey(window, keycode);
        return state == GLFW_PRESS || state == GLFW_REPEAT;
    }

    @Override
    protected boolean internalIsMouseButtonPressed(int button) {
        long window = Application.getWindow().getWindowHandle();
        int state = GLFW.glfwGetMouseButton(window, button);
        return state == GLFW_PRESS;
    }

    @Override
    protected Tuple<Double, Double> internalGetMousePosition() {
        long window = Application.getWindow().getWindowHandle();
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xPos, yPos);
        return Tuple.of(xPos.get(0), yPos.get(0));
    }

    @Override
    protected double internalgetMouseX() {
        return getMousePosition().getLeft();
    }

    @Override
    protected double internalgetMouseY() {
        return getMousePosition().getRight();
    }
}
