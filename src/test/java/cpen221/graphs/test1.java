package cpen221.graphs.graph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class test1 {

    private AdjacencyListGraph<Vertex, Edge<Vertex>> graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph<>();
    }

    // ==================== pathCost Tests ====================



    @Test
    public void testPathCostEmptyPath() {
        List<Vertex> emptyPath = new ArrayList<>();
        assertEquals(0.0, Algorithms.pathCost(graph, emptyPath, PathCostType.SUM_EDGES));
        assertEquals(0.0, Algorithms.pathCost(graph, emptyPath, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostSingleVertex() {
        Vertex v0 = new Vertex(0);
        graph.addVertex(v0);
        List<Vertex> path = List.of(v0);
        assertEquals(0.0, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
        assertEquals(0.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostSumEdges() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        List<Vertex> path = List.of(v0, v1, v2);
        assertEquals(8.0, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
    }

    @Test
    public void testPathCostMaxEdge() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        List<Vertex> path = List.of(v0, v1, v2);
        assertEquals(5.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }



    @Test
    public void testPathCostMaxEdgeSelectsMax() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 7.0));
        graph.addEdge(new Edge<>(v2, v3, 3.0));

        List<Vertex> path = List.of(v0, v1, v2, v3);
        assertEquals(7.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostTwoVertices() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge<>(v0, v1, 7.5));

        List<Vertex> path = List.of(v0, v1);
        assertEquals(7.5, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
        assertEquals(7.5, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostLongPath() {
        Vertex[] vertices = new Vertex[10];
        for (int i = 0; i < 10; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }
        for (int i = 0; i < 9; i++) {
            graph.addEdge(new Edge<>(vertices[i], vertices[i+1], 1.0 + i));
        }

        List<Vertex> path = Arrays.asList(vertices);
        double sum = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9;
        assertEquals(sum, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
        assertEquals(9.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    // ==================== minimumCostPath Tests ====================

    @Test
    public void testMinimumCostPathSourceEqualsSink() {
        Vertex v0 = new Vertex(0);
        graph.addVertex(v0);
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v0, PathCostType.SUM_EDGES);
        assertEquals(1, path.size());
        assertEquals(v0, path.get(0));
    }

    @Test
    public void testMinimumCostPathDisconnected() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v1, PathCostType.SUM_EDGES);
        assertTrue(path.isEmpty());
    }

    @Test
    public void testMinimumCostPathSimple() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge<>(v0, v1, 5.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v1, PathCostType.SUM_EDGES);
        assertEquals(2, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v1, path.get(1));
    }

    @Test
    public void testMinimumCostPathMultipleEdges() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 4.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v1, path.get(1));
        assertEquals(v3, path.get(2));
    }

    @Test
    public void testMinimumCostPathMaxEdgeMetric() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 5.0));
        graph.addEdge(new Edge<>(v2, v3, 5.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.MAX_EDGE);
        assertEquals(3, path.size());
        assertEquals(v2, path.get(1));
    }

    @Test
    public void testMinimumCostPathComplexNetwork() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3), v4 = new Vertex(4);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 4.0));
        graph.addEdge(new Edge<>(v1, v3, 2.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));
        graph.addEdge(new Edge<>(v1, v4, 10.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v4, PathCostType.SUM_EDGES);
        assertEquals(4, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v4, path.get(3));
        double cost = Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES);
        assertEquals(4.0, cost);
    }

    @Test
    public void testMinimumCostPathVeryLongMinPath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v3, 100.0));
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(4, path.size());
        double cost = Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES);
        assertEquals(3.0, cost);
    }

    @Test
    public void testMinimumCostPathMaxEdgePrefersShortPath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v2, 100.0));
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.MAX_EDGE);
        assertEquals(3, path.size());
        assertEquals(v1, path.get(1));
    }

    @Test
    public void testMinimumCostPathDijkstraRelaxSum() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
    }

    @Test
    public void testMinimumCostPathDijkstraSkipsVisited() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v1, path.get(1));
        assertEquals(v2, path.get(2));
    }

    @Test
    public void testMinimumCostPathRelaxNoImprovement() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 10.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(2, path.size());
    }

    @Test
    public void testMinimumCostPathRelaxMaxNoImprovement() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));
        graph.addEdge(new Edge<>(v1, v2, 20.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.MAX_EDGE);
        assertEquals(2, path.size());
    }



    @Test
    public void testDiameterEmptyGraph() {
        assertEquals(0.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
        assertEquals(0.0, Algorithms.diameter(graph, PathCostType.MAX_EDGE));
    }

    @Test
    public void testDiameterSingleVertex() {
        Vertex v0 = new Vertex(0);
        graph.addVertex(v0);
        assertEquals(0.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterTwoVertices() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge<>(v0, v1, 5.0));

        assertEquals(5.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterLinearPath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        assertEquals(5.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterTriangleGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 1.0));

        assertEquals(1.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterMaxEdgeMetric() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v1, v2, 5.0));

        assertEquals(10.0, Algorithms.diameter(graph, PathCostType.MAX_EDGE));
    }

    @Test
    public void testDiameterEccentricityCalculation() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        assertEquals(3.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterCompleteGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v3, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        assertEquals(1.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterStarGraph() {
        Vertex hub = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(hub);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(hub, v1, 1.0));
        graph.addEdge(new Edge<>(hub, v2, 1.0));
        graph.addEdge(new Edge<>(hub, v3, 1.0));

        assertEquals(2.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterMaxEdgeStarGraph() {
        Vertex hub = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(hub);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(hub, v1, 5.0));
        graph.addEdge(new Edge<>(hub, v2, 3.0));

        assertEquals(5.0, Algorithms.diameter(graph, PathCostType.MAX_EDGE));
    }

    @Test
    public void testDiameterDisconnectedGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(1.0, dia);
    }

    @Test
    public void testDiameterMultipleComponents() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3), v4 = new Vertex(4), v5 = new Vertex(5);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(2.0, dia);
    }

    @Test
    public void testDiameterInfinityHandling() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(1.0, dia);
    }

    @Test
    public void testDiameterLargeLinearPath() {
        int size = 100;
        Vertex[] vertices = new Vertex[size];
        for (int i = 0; i < size; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }
        for (int i = 0; i < size - 1; i++) {
            graph.addEdge(new Edge<>(vertices[i], vertices[i+1], 1.0));
        }

        assertEquals(99.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }



    @Test
    public void testCenterEmptyGraph() {
        assertNull(Algorithms.center(graph, PathCostType.SUM_EDGES));
        assertNull(Algorithms.center(graph, PathCostType.MAX_EDGE));
    }

    @Test
    public void testCenterSingleVertex() {
        Vertex v0 = new Vertex(0);
        graph.addVertex(v0);
        assertEquals(v0, Algorithms.center(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testCenterTwoVertices() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge<>(v0, v1, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v0) || center.equals(v1));
    }

    @Test
    public void testCenterLinearPath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(v1, center);
    }

    @Test
    public void testCenterTriangleGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
        assertTrue(center.equals(v0) || center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterMaxEdgeMetric() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.MAX_EDGE);
        assertNotNull(center);
        assertTrue(center.equals(v0) || center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterEccentricityComparison() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterStarGraph() {
        Vertex hub = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(hub);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(hub, v1, 1.0));
        graph.addEdge(new Edge<>(hub, v2, 1.0));
        graph.addEdge(new Edge<>(hub, v3, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(hub, center);
    }

    @Test
    public void testCenterBestEccentricityUpdate() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3), v4 = new Vertex(4);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(v2, center);
    }

    @Test
    public void testCenterEvenPathGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterOddPathGraph() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3), v4 = new Vertex(4);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(v2, center);
    }

    @Test
    public void testCenterLargestComponentOnly() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
        assertTrue((center.equals(v0) || center.equals(v1)) || (center.equals(v2) || center.equals(v3)));
    }

    @Test
    public void testCenterWithUnequalWeights() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 10.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterComponentsSingleIsolated() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(0.0, dia);

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v0) || center.equals(v1));
    }

    @Test
    public void testRelaxBothMetricsOnSamePath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));
        graph.addEdge(new Edge<>(v2, v3, 5.0));

        List<Vertex> pathSum = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(10.0, Algorithms.pathCost(graph, pathSum, PathCostType.SUM_EDGES));

        List<Vertex> pathMax = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.MAX_EDGE);
        assertEquals(5.0, Algorithms.pathCost(graph, pathMax, PathCostType.MAX_EDGE));
    }

    @Test
    public void testRelaxSkipsExistingPath() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v1, v2, 10.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(2, path.size());
        assertEquals(v2, path.get(1));
    }

    @Test
    public void testLargeDenseGraph() {
        int size = 20;
        Vertex[] vertices = new Vertex[size];
        for (int i = 0; i < size; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < Math.min(i + 5, size); j++) {
                graph.addEdge(new Edge<>(vertices[i], vertices[j], 1.0));
            }
        }

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertTrue(dia > 0);

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
    }

    @Test
    public void testComplexGraphScenario() {
        Vertex[] vertices = new Vertex[5];
        for (int i = 0; i < 5; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }

        graph.addEdge(new Edge<>(vertices[0], vertices[1], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[2], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[3], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[4], 1.0));
        graph.addEdge(new Edge<>(vertices[1], vertices[2], 2.0));
        graph.addEdge(new Edge<>(vertices[2], vertices[3], 2.0));
        graph.addEdge(new Edge<>(vertices[3], vertices[4], 2.0));
        graph.addEdge(new Edge<>(vertices[4], vertices[1], 2.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, vertices[1], vertices[4], PathCostType.SUM_EDGES);
        assertNotNull(path);
        assertTrue(path.size() > 0);

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertTrue(dia > 0);

        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
    }

    @Test
    public void testGraphWithDifferentEdgeWeights() {
        Vertex v0 = new Vertex(0), v1 = new Vertex(1), v2 = new Vertex(2), v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 0.5));
        graph.addEdge(new Edge<>(v1, v2, 10.5));
        graph.addEdge(new Edge<>(v2, v3, 2.3));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(4, path.size());
        double cost = Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES);
        assertEquals(13.3, cost, 0.001);
    }
}
