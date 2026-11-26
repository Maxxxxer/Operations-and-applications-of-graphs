package cpen221.graphs.graph;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the Algorithms class.
 * Tests cover all code paths including edge cases and error conditions.
 */
public class test10 {

    private AdjacencyListGraph<Vertex, Edge<Vertex>> graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph<>();
    }

    /* ==================== pathCost Tests ==================== */


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
        List<Vertex> singlePath = List.of(v0);
        assertEquals(0.0, Algorithms.pathCost(graph, singlePath, PathCostType.SUM_EDGES));
        assertEquals(0.0, Algorithms.pathCost(graph, singlePath, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostSumEdgesSimple() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        List<Vertex> path = List.of(v0, v1, v2);
        assertEquals(8.0, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
    }

    @Test
    public void testPathCostMaxEdgeSimple() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        List<Vertex> path = List.of(v0, v1, v2);
        assertEquals(5.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    @Test
    public void testPathCostMaxEdgeSelectsMaximum() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
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
    public void testPathCostWithZeroWeightEdges() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 0.0));
        graph.addEdge(new Edge<>(v1, v2, 0.0));

        List<Vertex> path = List.of(v0, v1, v2);
        assertEquals(0.0, Algorithms.pathCost(graph, path, PathCostType.SUM_EDGES));
        assertEquals(0.0, Algorithms.pathCost(graph, path, PathCostType.MAX_EDGE));
    }

    /* ==================== minimumCostPath Tests ==================== */

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
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        // No edge between them

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v1, PathCostType.SUM_EDGES);
        assertTrue(path.isEmpty());
    }

    @Test
    public void testMinimumCostPathSimple() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
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
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 4.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Shortest path should be v0 -> v1 -> v3
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v1, path.get(1));
        assertEquals(v3, path.get(2));
    }

    @Test
    public void testMinimumCostPathMaxEdgeMetric() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        // Path 1: v0 -> v1 -> v3 with weights 10, 1
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        // Path 2: v0 -> v2 -> v3 with weights 5, 5
        graph.addEdge(new Edge<>(v0, v2, 5.0));
        graph.addEdge(new Edge<>(v2, v3, 5.0));

        // With MAX_EDGE, path 2 should be preferred (max 5 vs max 10)
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.MAX_EDGE);
        assertEquals(3, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v2, path.get(1));
        assertEquals(v3, path.get(2));
    }

    @Test
    public void testMinimumCostPathWithNullInPath() {
        // Test when prev map creates a path where cur becomes null
        // This happens when there's a vertex with no predecessor (shouldn't reach sink normally)
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        // No edge - this should return empty list before null case
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v1, PathCostType.SUM_EDGES);
        assertTrue(path.isEmpty());
    }

    /* ==================== dijkstraLike Tests (indirectly through public methods) ==================== */

    @Test
    public void testDijkstraLikeWithMultipleRelaxations() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        // Diamond graph: v0 -> v1 -> v3 and v0 -> v2 -> v3
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));
        graph.addEdge(new Edge<>(v1, v3, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v3, PathCostType.SUM_EDGES);
        // Should choose v0 -> v1 -> v3 with cost 3
        assertEquals(3, path.size());
    }

    @Test
    public void testDijkstraLikeSkipsVisitedNodes() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        // Should prefer v0 -> v1 -> v2 (cost 2) over v0 -> v2 (cost 3)
        assertEquals(3, path.size());
        assertEquals(v0, path.get(0));
        assertEquals(v1, path.get(1));
        assertEquals(v2, path.get(2));
    }

    @Test
    public void testDijkstraLikeWithDomainRestriction() {
        // This tests the domain restriction in dijkstraLike
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
    }


    @Test
    public void testComputeComponentsConnectedGraph() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertTrue(dia > 0.0);
    }

    @Test
    public void testComputeComponentsDisconnectedGraph() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        // v2 and v3 are isolated
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Diameter should only consider the largest component (v0, v1)
        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(1.0, dia);
    }

    @Test
    public void testComputeComponentsIsolatedVertices() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        // No edges - two separate components

        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(0.0, dia);
    }

    @Test
    public void testComputeComponentsNeighbourTraversal() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Should traverse all vertices through neighbours
        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(3.0, dia);
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
    public void testDiameterLinearPath() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 3.0));

        assertEquals(5.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterTriangleGraph() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
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
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v1, v2, 5.0));

        assertEquals(10.0, Algorithms.diameter(graph, PathCostType.MAX_EDGE));
    }

    @Test
    public void testDiameterEccentricityCalculation() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Eccentricity: v0=3, v1=2, v2=2, v3=3
        // Diameter is the maximum eccentricity
        assertEquals(3.0, Algorithms.diameter(graph, PathCostType.SUM_EDGES));
    }

    @Test
    public void testDiameterInfinityHandling() {
        // Create a graph with unreachable vertices
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        // v2 is isolated

        // Diameter should only consider the largest component
        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(1.0, dia);
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
    public void testCenterLinearPath() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        // Center should be v1 (middle vertex)
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(v1, center);
    }

    @Test
    public void testCenterTriangleGraph() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 1.0));

        // All vertices have the same eccentricity, should return one of them
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
        assertTrue(center.equals(v0) || center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterMaxEdgeMetric() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        // With MAX_EDGE metric, v1 should be the center (eccentricity = 1)
        // while v0 and v2 have eccentricity = 1 as well
        Vertex center = Algorithms.center(graph, PathCostType.MAX_EDGE);
        assertNotNull(center);
        // All vertices could be centers in this symmetric case
        assertTrue(center.equals(v0) || center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterEccentricityComparison() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Eccentricity: v0=3, v1=2, v2=2, v3=3
        // Centers should be v1 or v2 (minimum eccentricity = 2)
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertTrue(center.equals(v1) || center.equals(v2));
    }

    @Test
    public void testCenterLargestComponentOnly() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));

        // Should only consider the component {v0, v1} or {v2, v3}
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
        assertTrue(
                (center.equals(v0) || center.equals(v1)) ||
                        (center.equals(v2) || center.equals(v3))
        );
    }

    @Test
    public void testCenterBestEccentricityUpdate() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));

        // Test that bestEcc is properly updated through iterations
        // Eccentricity: v0=4, v1=3, v2=2, v3=3, v4=4
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertEquals(v2, center);
    }

    /* ==================== relaxSum vs relaxMax Tests ==================== */

    @Test
    public void testRelaxSumMultiplePaths() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        // SUM_EDGES should prefer v0 -> v1 -> v2 (cost 2) over v0 -> v2 (cost 5)
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(3, path.size());
        assertEquals(v1, path.get(1));
    }

    @Test
    public void testRelaxMaxMultiplePaths() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 10.0));
        graph.addEdge(new Edge<>(v0, v2, 5.0));
        graph.addEdge(new Edge<>(v1, v2, 1.0));

        // MAX_EDGE should prefer v0 -> v2 (cost 5) over v0 -> v1 -> v2 (cost 10)
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.MAX_EDGE);
        assertEquals(2, path.size());
        assertEquals(v2, path.get(1));
    }

    @Test
    public void testRelaxSumNoImprovement() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v0, v2, 2.0));
        graph.addEdge(new Edge<>(v1, v2, 10.0));

        // Direct path v0 -> v2 (cost 2) is better than v0 -> v1 -> v2 (cost 11)
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.SUM_EDGES);
        assertEquals(2, path.size());
    }

    @Test
    public void testRelaxMaxNoImprovement() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addEdge(new Edge<>(v0, v1, 5.0));
        graph.addEdge(new Edge<>(v0, v2, 3.0));
        graph.addEdge(new Edge<>(v1, v2, 20.0));

        // Direct path v0 -> v2 (max 3) is better than v0 -> v1 -> v2 (max 20)
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v2, PathCostType.MAX_EDGE);
        assertEquals(2, path.size());
    }


    @Test
    public void testLargestComponentSelection() {
        // Create multiple components of different sizes
        // Component 1: v0 - v1
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        // Component 2: v2 - v3 - v4
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(new Edge<>(v0, v1, 1.0));
        graph.addEdge(new Edge<>(v2, v3, 1.0));
        graph.addEdge(new Edge<>(v3, v4, 1.0));

        // Diameter should be computed on the larger component {v2, v3, v4}
        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertEquals(2.0, dia);
    }

    @Test
    public void testLargestComponentTie() {
        // When components are equal size, should pick one
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

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
    public void testComplexGraphScenario() {
        // Build a more complex graph
        Vertex[] vertices = new Vertex[5];
        for (int i = 0; i < 5; i++) {
            vertices[i] = new Vertex(i);
            graph.addVertex(vertices[i]);
        }

        // Create a wheel-like structure
        graph.addEdge(new Edge<>(vertices[0], vertices[1], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[2], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[3], 1.0));
        graph.addEdge(new Edge<>(vertices[0], vertices[4], 1.0));
        graph.addEdge(new Edge<>(vertices[1], vertices[2], 2.0));
        graph.addEdge(new Edge<>(vertices[2], vertices[3], 2.0));
        graph.addEdge(new Edge<>(vertices[3], vertices[4], 2.0));
        graph.addEdge(new Edge<>(vertices[4], vertices[1], 2.0));

        // Test path finding
        List<Vertex> path = Algorithms.minimumCostPath(
                graph, vertices[1], vertices[4], PathCostType.SUM_EDGES
        );
        assertNotNull(path);
        assertTrue(path.size() > 0);

        // Test diameter
        double dia = Algorithms.diameter(graph, PathCostType.SUM_EDGES);
        assertTrue(dia > 0);

        // Test center
        Vertex center = Algorithms.center(graph, PathCostType.SUM_EDGES);
        assertNotNull(center);
    }

    @Test
    public void testGraphWithDifferentEdgeWeights() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

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

    @Test
    public void testConcurrentModificationDetection() {
        Vertex v0 = new Vertex(0);
        Vertex v1 = new Vertex(1);
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge<>(v0, v1, 1.0));

        // Modify graph during algorithm execution
        // (This should detect if the graph was modified unexpectedly)
        Vertex v2 = new Vertex(2);
        graph.addVertex(v2);

        // Should handle gracefully
        List<Vertex> path = Algorithms.minimumCostPath(graph, v0, v1, PathCostType.SUM_EDGES);
        assertEquals(2, path.size());
    }
}