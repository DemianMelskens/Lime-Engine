package org.lime.core;

import org.lime.core.utils.Tuple;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    public static boolean isKeyPressed(int keycode) {
        long window = Application.getInstance().getWindow().getWindowHandle();
        int state = GLFW.glfwGetKey(window, keycode);
        return state == GLFW_PRESS || state == GLFW_REPEAT;
    }

    public static boolean isMouseButtonPressed(int button) {
        long window = Application.getInstance().getWindow().getWindowHandle();
        int state = GLFW.glfwGetMouseButton(window, button);
        return state == GLFW_PRESS;
    }

    public static Tuple<Double, Double> getMousePosition() {
        long window = Application.getInstance().getWindow().getWindowHandle();
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xPos, yPos);
        return Tuple.of(xPos.get(0), yPos.get(0));
    }

    public static double getMouseX() {
        return getMousePosition().getLeft();
    }

    public static double getMouseY() {
        return getMousePosition().getRight();
    }
}
