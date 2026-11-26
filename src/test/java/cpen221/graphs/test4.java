package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extra tests to cover exception-throwing branches in
 * Algorithms / AdjacencyListGraph / AdjacencyMatrixGraph.
 */
public class test4 {



    @Test
    public void testAdjListAddVertexNullThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        // Objects.requireNonNull(v) in addVertex
        assertThrows(NullPointerException.class, () -> g.addVertex(null));
    }

    @Test
    public void testAdjListAddEdgeNullThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        g.addVertex(v1);
        // Objects.requireNonNull(e) in addEdge
        assertThrows(NullPointerException.class, () -> g.addEdge(null));
    }

    @Test
    public void testAdjListAddEdgeMissingEndpointReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);


        assertFalse(g.addEdge(e12));
    }



    @Test
    public void testMatrixImmutabilityMethodsThrow() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);

        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2));
        Set<Edge<Vertex>> es = new HashSet<>(Collections.singletonList(e));
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g =
                new AdjacencyMatrixGraph<>(vs, es);

        assertThrows(UnsupportedOperationException.class, () -> g.addVertex(new Vertex(3)));
        assertThrows(UnsupportedOperationException.class, () -> g.addEdge(new Edge<>(v1, v2, 2.0)));
        assertThrows(UnsupportedOperationException.class, () -> g.removeEdge(e));
        assertThrows(UnsupportedOperationException.class, () -> g.removeEdge(v1, v2));
        assertThrows(UnsupportedOperationException.class, () -> g.removeVertex(v1));
    }

    /* ====================== Algorithms.pathCost throws ====================== */

    @Test
    public void testPathCostNullArgumentsThrow() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        List<Vertex> empty = Collections.emptyList();

        assertThrows(NullPointerException.class,
                () -> Algorithms.pathCost(null, empty, PathCostType.SUM_EDGES));

        assertThrows(NullPointerException.class,
                () -> Algorithms.pathCost(g, null, PathCostType.SUM_EDGES));

        assertThrows(NullPointerException.class,
                () -> Algorithms.pathCost(g, empty, null));
    }

    @Test
    public void testPathCostNullVertexInPathThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        g.addVertex(v1);

        List<Vertex> path = Arrays.asList(v1, null);
        assertThrows(NullPointerException.class,
                () -> Algorithms.pathCost(g, path, PathCostType.SUM_EDGES));
    }

    @Test
    public void testPathCostNegativeEdgeLengthThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);


        class BadEdge extends Edge<Vertex> {
            BadEdge(Vertex a, Vertex b) {
                super(a, b, 1.0);
            }
            @Override
            public double length() {
                return -1.0;
            }
        }

        Edge<Vertex> bad = new BadEdge(v1, v2);
        assertTrue(g.addEdge(bad));

        List<Vertex> path = Arrays.asList(v1, v2);


        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.pathCost(g, path, PathCostType.SUM_EDGES));
    }

    @Test
    public void testMinimumCostPathSourceNotInGraphThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);


        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.minimumCostPath(g, v2, v1, PathCostType.SUM_EDGES));
    }

    @Test
    public void testMinimumCostPathNullSourceThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        g.addVertex(v1);


        assertThrows(NullPointerException.class,
                () -> Algorithms.minimumCostPath(g, null, v1, PathCostType.SUM_EDGES));
    }
}