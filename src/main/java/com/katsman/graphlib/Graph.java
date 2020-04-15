package com.katsman.graphlib;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public interface Graph<T> {

    void addVertex(@NotNull Vertex<T> vertex);

    void addEdge(@NotNull Edge<T> edge);

    List<Edge<T>> getPath(@NotNull Vertex<T> from, @NotNull Vertex<T> to);

    List<Edge<T>> getOptimalPath(@NotNull Vertex<T> from, @NotNull Vertex<T> to);
}
