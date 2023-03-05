package org.lime.platform.mac;

import org.lime.core.Input;
import org.lime.core.utils.Tuple;

public class MacInput extends Input {

    public MacInput() {
        throw new UnsupportedOperationException("Mac is not supported yet!");
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
