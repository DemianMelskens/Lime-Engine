package org.lime.core;

import org.lime.core.utils.Tuple;
import org.lime.platform.linux.LinuxInput;
import org.lime.platform.mac.MacInput;
import org.lime.platform.windows.WindowsInput;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public abstract class Input {

    private static Input instance;

    public static boolean isKeyPressed(int keycode) {
        return getInstance().internalIsKeyPressed(keycode);
    }

    public static boolean isMouseButtonPressed(int button) {
        return getInstance().internalIsMouseButtonPressed(button);
    }

    public static Tuple<Double, Double> getMousePosition() {
        return getInstance().internalGetMousePosition();
    }

    public static double getMouseX() {
        return getInstance().internalgetMouseX();
    }

    public static double getMouseY() {
        return getInstance().internalgetMouseY();
    }

    private static Input getInstance() {
        if (instance == null) {
            OperationSystem.Vendor vendor = OperationSystem.getVendor();
            LM_CORE_ASSERT(vendor != null, "OperatingSystem not supported");
            instance = switch (vendor) {
                case Windows -> new WindowsInput();
                case MacOS -> new MacInput();
                case Linux -> new LinuxInput();
            };
        }
        return instance;
    }

    protected abstract boolean internalIsKeyPressed(int keycode);

    protected abstract boolean internalIsMouseButtonPressed(int button);

    protected abstract Tuple<Double, Double> internalGetMousePosition();

    protected abstract double internalgetMouseX();

    protected abstract double internalgetMouseY();
}
