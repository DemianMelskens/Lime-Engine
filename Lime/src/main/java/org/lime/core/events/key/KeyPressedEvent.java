package org.lime.core.events.key;

import lombok.Getter;
import org.lime.core.events.EventType;

@Getter
public class KeyPressedEvent extends KeyEvent {

    private final int repeatCount;

    public KeyPressedEvent(int keyCode, int repeatCount) {
        super(keyCode);
        this.repeatCount = repeatCount;
    }

    @Override
    public EventType getEventType() {
        return EventType.KEY_PRESSED;
    }

    @Override
    public String toString() {
        return String.format("%s: %d, %d", getEventType().toString(), this.keyCode, this.repeatCount);
    }
}
