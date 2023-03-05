package org.lime.core.renderer.buffers;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Getter
public class BufferLayout implements Iterable<BufferElement> {
    private List<BufferElement> elements;
    private int stride = 0;

    public static BufferLayout create(BufferElement... elements) {
        return new BufferLayout(List.of(elements));
    }

    private BufferLayout(List<BufferElement> elements) {
        this.elements = elements;
        calculateOffsetsAndStride();
    }

    private void calculateOffsetsAndStride() {
        int offset = 0;
        stride = 0;
        for (BufferElement element : elements) {
            element.setOffset(offset);
            offset += element.getSize();
            stride += element.getSize();
        }
    }

    @Override
    public Iterator<BufferElement> iterator() {
        return this.elements.iterator();
    }

    @Override
    public void forEach(Consumer<? super BufferElement> action) {
        this.elements.forEach(action);
    }

    @Override
    public Spliterator<BufferElement> spliterator() {
        return this.elements.spliterator();
    }
}
