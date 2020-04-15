package com.katsman.graphlib;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public class Edge<T> {

    @NotNull
    private final Vertex<T> from;

    @NotNull
    private final Vertex<T> to;

    public Edge(@NotNull Vertex<T> from, @NotNull Vertex<T> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        this.from = from;
        this.to = to;
    }

    @NotNull
    public Vertex<T> getFrom() {
        return from;
    }

    @NotNull
    public Vertex<T> getTo() {
        return to;
    }

    public Edge<T> getReverseEdge() {
        return new Edge<>(to, from);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Edge<?> edge = (Edge<?>) obj;
        return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return from.toString() + "->" + to.toString();
    }
}
