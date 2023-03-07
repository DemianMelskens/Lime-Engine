package org.lime.platform.mac;

import org.lime.core.time.Time;

public class MacTime extends Time {

    public MacTime() {
        throw new UnsupportedOperationException("Mac is not supported yet!");
    }

    @Override
    public float internalGetTime() {
        return 0;
    }
}
