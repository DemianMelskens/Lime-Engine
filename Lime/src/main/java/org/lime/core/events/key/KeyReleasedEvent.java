package org.lime.core.events.key;

import org.lime.core.events.EventType;

public class KeyReleasedEvent extends KeyEvent {

    public KeyReleasedEvent(int keyCode) {
        super(keyCode);
    }

    @Override
    public EventType getEventType() {
        return EventType.KEY_RELEASED;
    }
}
