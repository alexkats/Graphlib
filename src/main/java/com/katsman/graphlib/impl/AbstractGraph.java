package com.katsman.graphlib.impl;

import com.katsman.graphlib.Edge;
import com.katsman.graphlib.Graph;
import com.katsman.graphlib.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 */

public abstract class AbstractGraph<T> implements Graph<T> {

    private final Map<Vertex<T>, Set<Vertex<T>>> edges;
    private final ReadWriteLock readWriteLock;

    protected AbstractGraph() {
        edges = new ConcurrentHashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public void addVertex(@NotNull Vertex<T> vertex) {
        Objects.requireNonNull(vertex);
        readWriteLock.readLock().lock();

        if (vertexExists(vertex)) {
            readWriteLock.readLock().unlock();
            throw new IllegalArgumentException("Provided vertex already exists");
        }

        readWriteLock.readLock().unlock();
        readWriteLock.writeLock().lock();
        edges.put(vertex, new HashSet<>());
        readWriteLock.writeLock().unlock();
    }

    @Override
    public void addEdge(@NotNull Edge<T> edge) {
        Objects.requireNonNull(edge);
        readWriteLock.readLock().lock();

        if (!edgeBetweenExistingVertices(edge)) {
            readWriteLock.readLock().unlock();
            throw new IllegalArgumentException("At least one vertex in the provided edge does not exist");
        }

        readWriteLock.readLock().unlock();

        try {
            readWriteLock.writeLock().lock();
            addEdge0(edge);
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    protected abstract void addEdge0(Edge<T> edge);

    @Override
    public List<Edge<T>> getPath(@NotNull Vertex<T> from, @NotNull Vertex<T> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        try {
            readWriteLock.readLock().lock();

            if (!vertexExists(from) || !vertexExists(to)) {
                throw new IllegalArgumentException("At least one of the provided vertices does not exist");
            }

            List<Edge<T>> ans = new LinkedList<>();
            Set<Vertex<T>> visited = new HashSet<>();

            dfs(from, ans, visited, to);

            if (ans.isEmpty() && !Objects.equals(from, to)) {
                throw new IllegalStateException("There is no path exists between the provided vertices");
            }

            return ans;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Edge<T>> getOptimalPath(@NotNull Vertex<T> from, @NotNull Vertex<T> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        try {
            readWriteLock.readLock().lock();

            if (!vertexExists(from) || !vertexExists(to)) {
                throw new IllegalStateException("At least one of the provided vertices does not exist");
            }

            List<Edge<T>> ans = new LinkedList<>();
            Set<Vertex<T>> visited = new HashSet<>();
            Queue<Vertex<T>> queue = new LinkedList<>();
            Map<Vertex<T>, VertexAndDistance> pathFrom = new HashMap<>();
            queue.add(from);
            visited.add(from);
            pathFrom.put(from, new VertexAndDistance(null, 0));

            while (!queue.isEmpty()) {
                Vertex<T> currentVertex = queue.remove();

                edges.get(currentVertex).forEach(nextVertex -> {
                    if (visited.contains(nextVertex)) {
                        return;
                    }

                    visited.add(nextVertex);
                    queue.add(nextVertex);

                    pathFrom.compute(nextVertex, (k, v) -> {
                        if (v == null) {
                            v = new VertexAndDistance(currentVertex, pathFrom.get(currentVertex).distance + 1);
                        } else {
                            if (v.distance > pathFrom.get(currentVertex).distance + 1) {
                                v.vertex = currentVertex;
                                v.distance++;
                            }
                        }

                        return v;
                    });
                });
            }

            if (!pathFrom.containsKey(to)) {
                throw new IllegalStateException("There is no path exists between the provided vertices");
            }

            Vertex<T> currentToVertex = to;
            Vertex<T> currentFromVertex = null;

            while (!Objects.equals(currentFromVertex, from)) {
                currentFromVertex = pathFrom.get(currentToVertex).vertex;
                ans.add(0, new Edge<>(currentFromVertex, currentToVertex));
                currentToVertex = currentFromVertex;
            }

            return ans;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private Vertex<T> dfs(Vertex<T> currentVertex, List<Edge<T>> ans, Set<Vertex<T>> visited, Vertex<T> finalVertex) {
        if (Objects.equals(currentVertex, finalVertex)) {
            return currentVertex;
        }

        visited.add(currentVertex);
        Set<Vertex<T>> edgesFromCurrentVertex = edges.get(currentVertex);

        for (Vertex<T> nextVertex : edgesFromCurrentVertex) {
            if (visited.contains(nextVertex)) {
                continue;
            }

            Vertex<T> result = dfs(nextVertex, ans, visited, finalVertex);

            if (result != null) {
                ans.add(0, new Edge<>(currentVertex, result));
                return currentVertex;
            }
        }

        return null;
    }

    protected boolean edgeExists(Vertex<T> from, Vertex<T> to) {
        return edges.containsKey(from) && edges.get(from).contains(to);
    }

    private boolean edgeBetweenExistingVertices(Edge<T> edge) {
        return vertexExists(edge.getFrom()) && vertexExists(edge.getTo());
    }

    private boolean vertexExists(Vertex<T> vertex) {
        return edges.containsKey(vertex);
    }

    protected void addEdgeToMap(Edge<T> edge) {
        edges.get(edge.getFrom()).add(edge.getTo());
    }

    private class VertexAndDistance {
        private Vertex<T> vertex;
        private int distance;

        private VertexAndDistance(Vertex<T> vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}
