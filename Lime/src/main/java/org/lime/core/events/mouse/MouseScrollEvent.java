package org.lime.core.events.mouse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;
import org.lime.core.events.EventType;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MouseScrollEvent extends Event {

    private final float xOffset;
    private final float yOffset;

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_SCROLL;
    }

    @Override
    public List<EventCategory> getCategories() {
        return List.of(EventCategory.MOUSE, EventCategory.INPUT);
    }

    @Override
    public String toString() {
        return String.format("%s: %f, %f", getEventType().toString(), this.xOffset, this.yOffset);
    }
}
