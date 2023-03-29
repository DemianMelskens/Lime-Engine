package org.lime.core;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lime.core.events.Event;
import org.lime.core.time.TimeStep;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Layer {

    private final String name;

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void onUpdate(TimeStep timestep);

    public abstract void onImGuiRender();

    public abstract void onEvent(Event event);
}
