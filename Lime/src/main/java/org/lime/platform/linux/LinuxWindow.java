package org.lime.platform.linux;

import org.lime.core.Window;

public class LinuxWindow extends Window {
    public LinuxWindow(String title, int width, int height) {
        super(title, width, height);
        throw new UnsupportedOperationException("Linux is not supported yet!");
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
    protected void shutdown() {

    }
}
