package org.lime.platform.linux;

import org.lime.core.Input;
import org.lime.core.utils.Tuple;

public class LinuxInput extends Input {

    public LinuxInput() {
        throw new UnsupportedOperationException("Linux is not supported yet!");
    }

    @Override
    protected boolean internalIsKeyPressed(int keycode) {
        return false;
    }

    @Override
    protected boolean internalIsMouseButtonPressed(int button) {
        return false;
    }

    @Override
    protected Tuple<Double, Double> internalGetMousePosition() {
        return null;
    }

    @Override
    protected double internalgetMouseX() {
        return 0;
    }

    @Override
    protected double internalgetMouseY() {
        return 0;
    }
}
