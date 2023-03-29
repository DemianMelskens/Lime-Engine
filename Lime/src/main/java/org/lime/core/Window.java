package org.lime.core;

import lombok.Getter;
import org.lime.core.events.Event;
import org.lime.core.renderer.GraphicsContext;
import org.lime.platform.linux.LinuxWindow;
import org.lime.platform.mac.MacWindow;
import org.lime.platform.windows.WindowsWindow;

import java.util.function.Consumer;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

@Getter
public abstract class Window {

    public static Window create(String title) {
        return create(title, 1280, 720);
    }

    public static Window create(String title, int width, int height) {
        OperationSystem.Vendor vendor = OperationSystem.getVendor();
        LM_CORE_ASSERT(vendor != null, "OperatingSystem not supported");
        return switch (vendor) {
            case Windows -> new WindowsWindow(title, width, height);
            case MacOS -> new MacWindow(title, width, height);
            case Linux -> new LinuxWindow(title, width, height);
        };
    }

    protected String title;
    protected int width;
    protected int height;
    protected boolean vSync;
    protected Consumer<Event> eventCallback;
    protected long windowHandle;
    protected GraphicsContext context;

    protected Window(String title, int width, int height) {
        init(title, width, height);
    }

    public abstract void setVSync(boolean enabled);

    protected void setEventCallback(Consumer<Event> eventCallback) {
        this.eventCallback = eventCallback;
    }

    protected abstract void init(String title, int width, int height);

    protected abstract void onUpdate();

    public abstract void shutdown();
}
