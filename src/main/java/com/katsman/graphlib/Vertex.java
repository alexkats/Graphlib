package com.katsman.graphlib;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public class Vertex<T> {

    private final T value;

    public Vertex(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Vertex<?> n = (Vertex<?>) obj;
        return Objects.equals(value, n.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Vertex(%s)", value);
    }
}
