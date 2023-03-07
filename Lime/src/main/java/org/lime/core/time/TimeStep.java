package org.lime.core.time;

public class TimeStep {

    private float time;

    public TimeStep(float time) {
        this.time = time;
    }

    public float getSeconds() {
        return time;
    }

    public float getMilliseconds() {
        return time * 1000.0f;
    }
}
