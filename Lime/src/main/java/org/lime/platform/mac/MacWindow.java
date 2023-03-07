package org.lime.platform.mac;

import org.lime.core.Window;

public class MacWindow extends Window {
    public MacWindow(String title, int width, int height) {
        super(title, width, height);
        throw new UnsupportedOperationException("Mac is not supported yet!");
    }

    @Override
    public void setVSync(boolean enabled) {

    }

    @Override
    protected void init(String title, int width, int height) {

    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void tearDown() {

    }
}
