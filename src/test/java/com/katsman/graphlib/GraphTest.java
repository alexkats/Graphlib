package com.katsman.graphlib;

import com.katsman.graphlib.impl.DirectedGraph;
import com.katsman.graphlib.impl.UndirectedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexey Katsman
 * @since 4/14/20
 *
 * Here are some tests for the graph library. I decided not to split all these tests into two classes just for
 * convenience of using some common parts.
 *
 * Also I intentionally had not written concurrency stress tests for this task because doing it properly
 * is really time-consuming.
 */

public class GraphTest {

    private Graph<Integer> directedGraph;
    private Graph<Integer> undirectedGraph;

    @Before
    public void init() {
        directedGraph = new DirectedGraph<>();
        undirectedGraph = new UndirectedGraph<>();
    }

    @Test
    public void testBaseDirectedGraph() {
        Edge<Integer> addedEdge = addTwoVerticesAndOneEdge(directedGraph);

        List<Edge<Integer>> path = directedGraph.getPath(addedEdge.getFrom(), addedEdge.getTo());
        assertEquals(1, path.size());
        assertEquals(addedEdge, path.get(0));

        List<Edge<Integer>> optimalPath = directedGraph.getPath(addedEdge.getFrom(), addedEdge.getTo());
        assertEquals(1, optimalPath.size());
        assertEquals(addedEdge, optimalPath.get(0));
    }

    @Test
    public void testBaseUndirectedGraph() {
        Edge<Integer> addedEdge = addTwoVerticesAndOneEdge(undirectedGraph);

        List<Edge<Integer>> path = undirectedGraph.getPath(addedEdge.getFrom(), addedEdge.getTo());
        assertEquals(1, path.size());
        assertEquals(addedEdge, path.get(0));
        assertEquals(addedEdge.getFrom().getValue(), path.get(0).getFrom().getValue());
        assertEquals(addedEdge.getTo().getValue(), path.get(0).getTo().getValue());

        List<Edge<Integer>> reversePath = undirectedGraph.getPath(addedEdge.getTo(), addedEdge.getFrom());
        assertEquals(1, reversePath.size());
        assertEquals(addedEdge.getReverseEdge(), reversePath.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwoEqualVertices() {
        Vertex<Integer> v1 = new Vertex<>(1);
        Vertex<Integer> v2 = new Vertex<>(1);
        directedGraph.addVertex(v1);
        directedGraph.addVertex(v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwoEqualEdgesToDirectedGraph() {
        Edge<Integer> addedEdge = addTwoVerticesAndOneEdge(directedGraph);
        directedGraph.addEdge(new Edge<>(addedEdge.getFrom(), addedEdge.getTo()));
    }

    @Test
    public void testAddCycleBetweenTwoNodes() {
        Edge<Integer> addedEdge = addTwoVerticesAndOneEdge(directedGraph);
        directedGraph.addEdge(new Edge<>(addedEdge.getTo(), addedEdge.getFrom()));

        List<Edge<Integer>> path = directedGraph.getPath(addedEdge.getFrom(), addedEdge.getTo());
        assertEquals(1, path.size());
        assertEquals(addedEdge, path.get(0));

        List<Edge<Integer>> reversePath = directedGraph.getPath(addedEdge.getTo(), addedEdge.getFrom());
        assertEquals(1, reversePath.size());
        assertEquals(addedEdge.getReverseEdge(), reversePath.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwoEqualEdgesToUndirectedGraph() {
        Edge<Integer> addedEdge = addTwoVerticesAndOneEdge(undirectedGraph);
        undirectedGraph.addEdge(new Edge<>(addedEdge.getTo(), addedEdge.getFrom()));
    }

    @Test
    public void testPathWithCycle() {
        List<Vertex<Integer>> vertices = new ArrayList<>();
        List<Edge<Integer>> edges = new ArrayList<>();
        int n = 10;
        int startCycleIndex = 3;
        int endCycleIndex = 7;

        for (int i = 1; i <= n; i++) {
            vertices.add(new Vertex<>(i));
        }

        for (int i = startCycleIndex; i < endCycleIndex; i++) {
            edges.add(new Edge<>(vertices.get(i), vertices.get(i + 1)));
        }

        edges.add(new Edge<>(vertices.get(endCycleIndex), vertices.get(startCycleIndex)));
        edges.add(new Edge<>(vertices.get(0), vertices.get(startCycleIndex)));
        edges.add(new Edge<>(vertices.get(endCycleIndex - 1), vertices.get(n - 1)));

        vertices.forEach(directedGraph::addVertex);
        edges.forEach(directedGraph::addEdge);

        List<Edge<Integer>> path = directedGraph.getPath(vertices.get(0), vertices.get(n - 1));
        assertEquals(endCycleIndex - startCycleIndex + 1, path.size());
        assertTrue(path.contains(new Edge<>(vertices.get(0), vertices.get(startCycleIndex))));
        assertTrue(path.contains(new Edge<>(vertices.get(endCycleIndex - 1), vertices.get(n - 1))));
    }

    @Test(timeout = 2000L)
    public void testPerformance() {
        int n = 1000;
        int m = n * (n - 1) / 2;

        List<Vertex<Integer>> vertices = IntStream.range(0, n).mapToObj(Vertex::new).collect(Collectors.toList());
        List<Edge<Integer>> edges = new ArrayList<>(m);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                edges.add(new Edge<>(vertices.get(i), vertices.get(j)));
            }
        }

        Collections.shuffle(vertices);
        Collections.shuffle(edges);
        vertices.forEach(undirectedGraph::addVertex);
        edges.forEach(undirectedGraph::addEdge);

        List<Edge<Integer>> path = undirectedGraph.getPath(vertices.get(0), vertices.get(n - 1));
        assertEquals(vertices.get(0), path.get(0).getFrom());
        assertEquals(vertices.get(n - 1), path.get(path.size() - 1).getTo());

        List<Edge<Integer>> optimalPath = undirectedGraph.getOptimalPath(vertices.get(0), vertices.get(n - 1));
        assertEquals(1, optimalPath.size());
        assertEquals(new Edge<>(vertices.get(0), vertices.get(n - 1)), optimalPath.get(0));
    }

    private Edge<Integer> addTwoVerticesAndOneEdge(Graph<Integer> graph) {
        Vertex<Integer> v1 = new Vertex<>(1);
        Vertex<Integer> v2 = new Vertex<>(2);
        graph.addVertex(v1);
        graph.addVertex(v2);
        Edge<Integer> edgeToAdd = new Edge<>(v1, v2);
        graph.addEdge(edgeToAdd);
        return edgeToAdd;
    }
}
