package com.katsman.graphlib.impl;

import com.katsman.graphlib.Edge;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public class DirectedGraph<T> extends AbstractGraph<T> {

    @Override
    protected void addEdge0(Edge<T> edge) {
        if (edgeExists(edge.getFrom(), edge.getTo())) {
            throw new IllegalArgumentException("Provided edge already exists");
        }

        addEdgeToMap(edge);
    }
}
