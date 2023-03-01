package org.lime.core;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Layer {

    private final String name;

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void onImGuiRender();

    public void onEvent(Event event) {
    }
}
