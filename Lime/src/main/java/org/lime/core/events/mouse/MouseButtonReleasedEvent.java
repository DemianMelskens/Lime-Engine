package org.lime.core.events.mouse;

import org.lime.core.events.EventType;

public class MouseButtonReleasedEvent extends MouseButtonEvent {
    public MouseButtonReleasedEvent(int button) {
        super(button);
    }

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_BUTTON_RELEASED;
    }
}
