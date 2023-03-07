package org.lime.core.time;

import org.lime.core.OperationSystem;
import org.lime.platform.linux.LinuxTime;
import org.lime.platform.mac.MacTime;
import org.lime.platform.windows.WindowsTime;

import static org.lime.core.utils.Assert.LM_CORE_ASSERT;

public abstract class Time {

    private static Time instance;

    public static float getTime() {
        return getInstance().internalGetTime();
    }

    private static Time getInstance() {
        if (instance == null) {
            OperationSystem.Vendor vendor = OperationSystem.getVendor();
            LM_CORE_ASSERT(vendor != null, "OperatingSystem not supported");
            instance = switch (vendor) {
                case Windows -> new WindowsTime();
                case MacOS -> new MacTime();
                case Linux -> new LinuxTime();
            };
        }
        return instance;
    }

    protected abstract float internalGetTime();
}
