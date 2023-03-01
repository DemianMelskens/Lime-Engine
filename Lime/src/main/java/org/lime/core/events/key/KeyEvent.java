package org.lime.core.events.key;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;
import org.lime.core.events.EventCategory;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class KeyEvent extends Event {
    protected final int keyCode;

    @Override
    public List<EventCategory> getCategories() {
        return List.of(EventCategory.KEYBOARD, EventCategory.INPUT);
    }

    @Override
    public String toString() {
        return String.format("%s: %d", getEventType().toString(), this.keyCode);
    }
}
