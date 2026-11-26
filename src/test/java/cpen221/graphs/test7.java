package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Extra tests for leftover red lines:
 *  - Edge.incident / intersects / intersection / distinctVertex / constructors
 *  - Algorithms.pathCost invalid edge length
 *  - Algorithms.dijkstraLike: graph changed unexpectedly during search
 */
public class test7 {



    @Test
    public void testEdgeIncidentNullAndNonIncident() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);


        assertFalse(e.incident(null));
        assertTrue(e.incident(v1));
        assertTrue(e.incident(v2));
        assertFalse(e.incident(v3));
    }

    @Test
    public void testEdgeIntersectsVariants() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e23 = new Edge<>(v2, v3, 1.0);
        Edge<Vertex> e34 = new Edge<>(v3, v4, 1.0);

        assertFalse(e12.intersects(null));
        assertTrue(e12.intersects(e23));

        assertFalse(e12.intersects(e34));
    }


    @Test
    public void testEdgeIntersectionNullAndCommonV1() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e13 = new Edge<>(v1, v3, 1.0);

        // e == null -> throws
        assertThrows(NoSuchElementException.class,
                () -> e12.intersection(null));


        Vertex inter = e12.intersection(e13);
        assertEquals(v1, inter);
    }

    @Test
    public void testEdgeIntersectionCommonV2AndNoCommon() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e24 = new Edge<>(v2, v4, 1.0);
        Edge<Vertex> e34 = new Edge<>(v3, v4, 1.0);


        Vertex inter = e12.intersection(e24);
        assertEquals(v2, inter);


        assertThrows(NoSuchElementException.class,
                () -> e12.intersection(e34));
    }

    @Test
    public void testEdgeDistinctVertexBranches() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e23 = new Edge<>(v2, v3, 1.0);
        Edge<Vertex> e13 = new Edge<>(v1, v3, 1.0);

        // this.equals(e) -> throws
        assertThrows(NoSuchElementException.class,
                () -> e12.distinctVertex(e12));

        Vertex dv1 = e12.distinctVertex(e13);
        assertEquals(v2, dv1);


        Vertex dv2 = e12.distinctVertex(e23);
        assertEquals(v1, dv2);
    }

    @Test
    public void testEdgeConstructorNullVerticesThrow() {
        Vertex v = new Vertex(1);

        assertThrows(IllegalArgumentException.class,
                () -> new Edge<Vertex>(null, v, 1.0));

        assertThrows(IllegalArgumentException.class,
                () -> new Edge<Vertex>(v, null, 1.0));
    }

    @Test
    public void testEdgeConstructorSameVertexThrows() {
        Vertex v = new Vertex(1);
        assertThrows(IllegalArgumentException.class,
                () -> new Edge<Vertex>(v, v, 1.0));
    }

    @Test
    public void testPathCostInvalidEdgeLengthThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        Edge<Vertex> bad = new BadEdge(v1, v2, -5.0);
        assertTrue(g.addEdge(bad));

        List<Vertex> path = Arrays.asList(v1, v2);

        assertThrows(IllegalArgumentException.class,
                () -> g.pathCost(path, PathCostType.SUM_EDGES));
    }



    private static class BadEdge extends Edge<Vertex> {
        private final double fakeLen;

        BadEdge(Vertex v1, Vertex v2, double fakeLen) {
            super(v1, v2, 1.0);
            this.fakeLen = fakeLen;
        }

        @Override
        public double length() {
            return fakeLen;
        }
    }
    private static class BadGraphVertexDisappears
            extends AdjacencyListGraph<Vertex, Edge<Vertex>> {

        private int hasVertexCalls = 0;
        private int limit = Integer.MAX_VALUE;

        @Override
        public Set<Vertex> getVertices() {
            Set<Vertex> base = super.getVertices();
            limit = base.size();
            return base;
        }

        @Override
        public boolean hasVertex(Vertex v) {
            hasVertexCalls++;
            if (hasVertexCalls <= limit) {

                return super.hasVertex(v);
            }

            return false;
        }
    }

    @Test
    public void testDijkstraGraphChangedUnexpectedlyThrows() {
        BadGraphVertexDisappears g = new BadGraphVertexDisappears();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(new Edge<>(v1, v2, 1.0));

        assertThrows(IllegalStateException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }


    @Test
    public void testRelaxSumNullArgumentsThrows() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxSum",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, PriorityQueue.class
        );
        m.setAccessible(true);

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, null, null, null, null, null, null)
        );
        assertTrue(ite.getCause() instanceof NullPointerException);
    }

    @Test
    public void testRelaxSumMissingDistEntryThrows() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxSum",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, PriorityQueue.class
        );
        m.setAccessible(true);

        Vertex u = new Vertex(1);
        Vertex v = new Vertex(2);
        Edge<Vertex> e = new Edge<>(u, v, 1.0);

        Map<Vertex, Double> dist = new HashMap<>();
        dist.put(u, 0.0);
        Map<Vertex, Vertex> prev = new HashMap<>();
        PriorityQueue<?> pq = new PriorityQueue<>();

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, u, v, e, dist, prev, pq)
        );
        assertTrue(ite.getCause() instanceof IllegalArgumentException);
    }



    @Test
    public void testRelaxMaxNullArgumentsThrows() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxMax",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, PriorityQueue.class
        );
        m.setAccessible(true);

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, null, null, null, null, null, null)
        );
        assertTrue(ite.getCause() instanceof NullPointerException);
    }


}