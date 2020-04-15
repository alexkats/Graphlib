package com.katsman.graphlib.impl;

import com.katsman.graphlib.Edge;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public class UndirectedGraph<T> extends AbstractGraph<T> {

    @Override
    protected void addEdge0(Edge<T> edge) {
        if (edgeExists(edge.getFrom(), edge.getTo()) || edgeExists(edge.getTo(), edge.getFrom())) {
            throw new IllegalArgumentException("Provided edge already exists");
        }

        addEdgeToMap(edge);
        addEdgeToMap(edge.getReverseEdge());
    }
}
