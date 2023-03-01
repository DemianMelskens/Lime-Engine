package org.lime.core.events.application;

import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;
import org.lime.core.events.EventType;

import java.util.List;

public class WindowCloseEvent extends Event {
    @Override
    public EventType getEventType() {
        return EventType.WINDOW_CLOSE;
    }

    @Override
    public List<EventCategory> getCategories() {
        return List.of(EventCategory.APPLICATION);
    }
}
