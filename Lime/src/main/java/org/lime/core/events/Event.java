package org.lime.core.events;

import java.util.List;

public abstract class Event {

    public boolean isHandled = false;

    public abstract EventType getEventType();

    public abstract List<EventCategory> getCategories();

    @Override
    public String toString() {
        return getEventType().toString();
    }

    public boolean isInCategory(EventCategory category) {
        return getCategories().contains(category);
    }
}
