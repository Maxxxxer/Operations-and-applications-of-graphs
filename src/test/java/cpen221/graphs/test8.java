package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class test8 {

    private static final class BadEdge extends Edge<Vertex> {
        private final double badLen;

        BadEdge(Vertex v1, Vertex v2, double badLen) {
            super(v1, v2, 1.0);
            this.badLen = badLen;
        }

        @Override
        public double length() {
            return badLen;
        }
    }


    private AdjacencyListGraph<Vertex, Edge<Vertex>> oneEdgeGraph(
            Vertex v1, Vertex v2, Edge<Vertex> e) {

        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(e);
        return g;
    }


    @Test
    public void testPathCostNullVertexThrows() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        AdjacencyListGraph<Vertex, Edge<Vertex>> g =
                oneEdgeGraph(v1, v2, new Edge<>(v1, v2, 1.0));

        List<Vertex> badPath = Arrays.asList(v1, null, v2);

        assertThrows(NullPointerException.class,
                () -> Algorithms.pathCost(g, badPath, PathCostType.SUM_EDGES));
    }


    @Test
    public void testPathCostInvalidEdgeLengthThrows() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        Edge<Vertex> bad = new BadEdge(v1, v2, -1.0);
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = oneEdgeGraph(v1, v2, bad);

        List<Vertex> path = Arrays.asList(v1, v2);

        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.pathCost(g, path, PathCostType.SUM_EDGES));
    }


    @Test
    public void testRelaxSumThrowsOnNullArgs() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxSum",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, java.util.PriorityQueue.class);
        m.setAccessible(true);

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);

        Map<Vertex, Double> dist = new HashMap<>();
        Map<Vertex, Vertex> prev = new HashMap<>();
        java.util.PriorityQueue<?> pq = new java.util.PriorityQueue<>();


        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, null, v2, e, dist, prev, pq)
        );
        assertTrue(ite.getCause() instanceof NullPointerException);
    }


    @Test
    public void testRelaxSumDistMapMissingUVThrows() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxSum",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, java.util.PriorityQueue.class);
        m.setAccessible(true);

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);

        Map<Vertex, Double> dist = new HashMap<>();
        Map<Vertex, Vertex> prev = new HashMap<>();
        java.util.PriorityQueue<?> pq = new java.util.PriorityQueue<>();


        dist.put(v2, 0.0);

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, v1, v2, e, dist, prev, pq)
        );
        assertTrue(ite.getCause() instanceof IllegalArgumentException);
    }


    @Test
    public void testRelaxSumInvalidLengthThrows() throws Exception {
        Method m = Algorithms.class.getDeclaredMethod(
                "relaxSum",
                Vertex.class, Vertex.class, Edge.class,
                Map.class, Map.class, java.util.PriorityQueue.class);
        m.setAccessible(true);

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        // length() 返回负值
        Edge<Vertex> bad = new BadEdge(v1, v2, -1.0);

        Map<Vertex, Double> dist = new HashMap<>();
        Map<Vertex, Vertex> prev = new HashMap<>();
        java.util.PriorityQueue<?> pq = new java.util.PriorityQueue<>();

        dist.put(v1, 0.0);
        dist.put(v2, 100.0);

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, v1, v2, bad, dist, prev, pq)
        );
        assertTrue(ite.getCause() instanceof IllegalArgumentException);
    }


    @Test
    public void testDijkstraLikeRejectsNegativeEdgeLength() throws Exception {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> bad = new BadEdge(v1, v2, -1.0);

        AdjacencyListGraph<Vertex, Edge<Vertex>> g = oneEdgeGraph(v1, v2, bad);

        Set<Vertex> domain = new HashSet<>(Arrays.asList(v1, v2));
        Map<Vertex, Double> dist = new HashMap<>();
        Map<Vertex, Vertex> prev = new HashMap<>();


        for (Vertex v : domain) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }
        dist.put(v1, 0.0);

        Method m = Algorithms.class.getDeclaredMethod(
                "dijkstraLike",
                Graph.class, Vertex.class, PathCostType.class,
                Set.class, Map.class, Map.class);
        m.setAccessible(true);

        InvocationTargetException ite = assertThrows(
                InvocationTargetException.class,
                () -> m.invoke(null, g, v1, PathCostType.SUM_EDGES,
                        domain, dist, prev)
        );
        assertTrue(ite.getCause() instanceof IllegalArgumentException);
    }
    @Test
    public void testAdjListAddEdgeMissingVertexReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);


        g.addVertex(v1);
        Edge<Vertex> e = new Edge<>(v1, v2);

        assertFalse(g.addEdge(e));
        assertFalse(g.hasEdge(v1, v2));
    }

    @Test
    public void testAdjListAddEdgeDuplicateBetweenSameEndpointsReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        g.addVertex(v2);

        Edge<Vertex> e1 = new Edge<>(v1, v2);
        Edge<Vertex> e2 = new Edge<>(v1, v2);


        assertTrue(g.addEdge(e1));
        assertFalse(g.addEdge(e2));

        assertEquals(e1, g.getEdge(v1, v2));
    }



    @Test
    public void testAdjListHasEdgeWhenSourceVertexMissingIsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        assertFalse(g.hasEdge(v1, v2));
    }

    @Test
    public void testAdjListHasEdgeNoEdgeBetweenVerticesIsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);

        g.addEdge(new Edge<>(v1, v2));


        assertFalse(g.hasEdge(v1, v3));
    }



    @Test
    public void testAdjListRemoveEdgeNullReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);


        assertFalse(g.removeEdge((Edge<Vertex>) null));
    }

    @Test
    public void testAdjListRemoveEdgeNonexistentReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        Edge<Vertex> e = new Edge<>(v1, v2);


        assertFalse(g.removeEdge(e));
    }



    @Test
    public void testAdjListRemoveEdgeByVerticesWhenMissingReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);


        assertFalse(g.removeEdge(v1, v2));
    }



    @Test
    public void testAdjListGetEdgeMissingVertexReturnsNull() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        // m2 == null 分支
        assertNull(g.getEdge(v1, v2));
    }

    @Test
    public void testAdjListGetEdgeNoEdgeBetweenReturnsNull() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);

        g.addEdge(new Edge<>(v1, v2));


        assertNull(g.getEdge(v1, v3));
    }
    @Test
    public void testAdjListAddEdgeMissingEndpointReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);

        assertFalse(g.addEdge(e), "should be false");
        assertFalse(g.hasEdge(v1, v2), "should not be included");
    }

    // 覆盖：
    // Map<V,E> ma = adj.get(a1), mb = adj.get(b);
    // if (ma.containsKey(b)) return false;   // duplicate between endpoints
    @Test
    public void testAdjListAddEdgeDuplicateBetweenEndpointsReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        g.addVertex(v2);

        Edge<Vertex> e1 = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e2 = new Edge<>(v1, v2, 2.0);

        assertTrue(g.addEdge(e1), "success");
        assertFalse(g.addEdge(e2), "denied");


        assertTrue(g.hasEdge(v1, v2));
    }
    @Test
    public void testAdjListHasEdgeWhenV1NotInGraph() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        assertFalse(g.hasEdge(v1, v2), "should be false");
    }

    @Test
    public void testAdjListHasEdgeWhenNoEdgeBetweenVertices() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);

        g.addEdge(new Edge<>(v1, v3, 1.0));

        assertFalse(g.hasEdge(v1, v2),
                "cover the branch");
    }
    @Test
    public void testAdjListRemoveEdgeNullAndNonExisting() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        g.addVertex(v2);

        Edge<Vertex> e = new Edge<>(v1, v2, 1.0);

        assertFalse(g.removeEdge(e), "should be false");

        assertFalse(g.removeEdge(null), "should be false");
    }
    @Test
    public void testAdjListRemoveEdgeByVerticesWhenMissing() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);

        // 只加 v1-v3
        g.addEdge(new Edge<>(v1, v3, 1.0));

        // v1-v2 没有边 -> getEdge 返回 null，走到 && 前半部分为 false
        assertFalse(g.removeEdge(v1, v2));
    }
    @Test
    public void testAdjListGetEdgeWhenFirstVertexNotPresent() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        assertNull(g.getEdge(v1, v2));
    }

}

