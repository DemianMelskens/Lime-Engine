package org.lime.platform.linux;

import org.lime.core.time.Time;

public class LinuxTime extends Time {

    public LinuxTime() {
        throw new UnsupportedOperationException("Linux is not supported yet!");
    }

    @Override
    public float internalGetTime() {
        return 0;
    }
}
