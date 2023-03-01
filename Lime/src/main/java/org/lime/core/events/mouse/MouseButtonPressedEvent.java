package org.lime.core.events.mouse;

import lombok.Getter;
import org.lime.core.events.EventType;

@Getter
public class MouseButtonPressedEvent extends MouseButtonEvent {

    public MouseButtonPressedEvent(int button) {
        super(button);
    }

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_BUTTON_PRESSED;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", getEventType().toString(), this.button);
    }
}
