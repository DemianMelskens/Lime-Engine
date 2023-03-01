package org.lime.core.events.mouse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MouseButtonEvent extends Event {
    protected final int button;

    @Override
    public List<EventCategory> getCategories() {
        return List.of(EventCategory.MOUSE_BUTTON, EventCategory.MOUSE, EventCategory.INPUT);
    }

    @Override
    public String toString() {
        return String.format("%s: %d", getEventType().toString(), this.button);
    }
}