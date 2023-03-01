package org.lime.core.events;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class Event {

    protected boolean handled = false;

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
