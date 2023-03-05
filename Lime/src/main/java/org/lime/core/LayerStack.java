package org.lime.core;

import java.util.*;
import java.util.function.Consumer;

public class LayerStack implements Iterable<Layer> {
    private final ArrayList<Layer> layers;
    private int layerInsertIndex;

    public LayerStack() {
        this.layers = new ArrayList<>();
        this.layerInsertIndex = 0;
    }

    public void pushLayer(Layer layer) {
        this.layers.add(layerInsertIndex++, layer);
        layer.onAttach();
    }

    public void pushOverlay(Layer layer) {
        this.layers.add(layer);
        layer.onAttach();
    }

    public void popLayer(Layer layer) {
        int index = this.layers.indexOf(layer);
        if (index >= 0) {
            this.layers.remove(index);
            this.layerInsertIndex--;
            layer.onDetach();
        }
    }

    public void popOverlay(Layer layer) {
        int index = this.layers.indexOf(layer);
        if (index >= 0) {
            this.layers.remove(index);
            layer.onDetach();
        }
    }

    @Override
    public Iterator<Layer> iterator() {
        return layers.iterator();
    }

    @Override
    public void forEach(Consumer<? super Layer> action) {
        this.layers.forEach(action);
    }

    @Override
    public Spliterator<Layer> spliterator() {
        return this.layers.spliterator();
    }
}
