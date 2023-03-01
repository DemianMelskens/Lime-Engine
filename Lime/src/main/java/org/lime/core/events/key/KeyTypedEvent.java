package org.lime.core.events.key;

import org.lime.core.events.EventType;

public class KeyTypedEvent extends KeyEvent {
    public KeyTypedEvent(int keyCode) {
        super(keyCode);
    }

    @Override
    public EventType getEventType() {
        return EventType.KEY_TYPED;
    }
}
