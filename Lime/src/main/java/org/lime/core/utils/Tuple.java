package org.lime.core.utils;

import lombok.Getter;

@Getter
public class Tuple<L, R> {
    private final L left;
    private final R right;

    public static <L, R> Tuple<L, R> of(L left, R right) {
        return new Tuple<>(left, right);
    }

    private Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
