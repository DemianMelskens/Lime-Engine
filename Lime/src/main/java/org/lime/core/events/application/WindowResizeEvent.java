package org.lime.core.events.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;
import org.lime.core.events.EventType;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class WindowResizeEvent extends Event {

    private final int width;
    private final int height;

    @Override
    public EventType getEventType() {
        return EventType.WINDOW_RESIZE;
    }

    @Override
    public List<EventCategory> getCategories() {
        return List.of(EventCategory.APPLICATION);
    }

    @Override
    public String toString() {
        return String.format("%s: %d, %d", getEventType().toString(), this.width, this.height);
    }
}
