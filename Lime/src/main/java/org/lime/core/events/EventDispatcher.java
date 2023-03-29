package org.lime.core.events;


import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class EventDispatcher {

    private final Event event;

    public <T extends Event> boolean dispatch(Predicate<T> predicate) {
        try {
            this.event.isHandled = predicate.test((T) this.event);
            return true;
        } catch (ClassCastException ignored) {
        }

        return false;
    }
}
