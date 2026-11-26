package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//test 2 (main test)
public class test2{

    private AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g;
    private Vertex v1, v2, v3, v4, v5;
    private Edge<Vertex> e12, e23, e34, e13, e24, e45;

    @BeforeEach
    public void setup() {
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
        v4 = new Vertex(4);
        v5 = new Vertex(5);

        // weights carefully chosen so that:
        // - for SUM_EDGES: v1->v2->v3 is cheaper than v1->v3 directly
        // - for MAX_EDGE:  v1->v3 directly is better than via v2
        e12 = new Edge<>(v1, v2, 1.0);
        e23 = new Edge<>(v2, v3, 1.0);
        e34 = new Edge<>(v3, v4, 3.0);
        e13 = new Edge<>(v1, v3, 5.0);
        e24 = new Edge<>(v2, v4, 2.5);
        e45 = new Edge<>(v4, v5, 1.5);

        Set<Vertex> vertices = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5));
        Set<Edge<Vertex>> edges = new HashSet<>(Arrays.asList(e12, e23, e34, e13, e24, e45));
        g = new AdjacencyMatrixGraph<>(vertices, edges);
    }


    @Test
    public void testHasVertexAndHasEdgeByObject() {
        assertTrue(g.hasVertex(v1));
        assertTrue(g.hasVertex(v5));
        assertFalse(g.hasVertex(new Vertex(999)));

        assertTrue(g.hasEdge(e12));
        assertTrue(g.hasEdge(e45));
        // not stored edge
        Edge<Vertex> fake = new Edge<>(v1, v5, 10.0);
        assertFalse(g.hasEdge(fake));
    }

    @Test
    public void testHasEdgeByVerticesAndEdgeLength() {
        assertTrue(g.hasEdge(v1, v2));
        assertTrue(g.hasEdge(v2, v1)); // undirected

        assertFalse(g.hasEdge(v1, v5));

        assertEquals(1.0, g.edgeLength(v1, v2), 1e-9);
        assertEquals(1.5, g.edgeLength(v4, v5), 1e-9);
    }

    @Test
    public void testGetVerticesAndEdges() {
        Set<Vertex> vs = g.getVertices();
        Set<Edge<Vertex>> es = g.getEdges();

        assertEquals(5, vs.size());
        assertTrue(vs.contains(v1));
        assertTrue(vs.contains(v5));

        assertEquals(6, es.size());
        assertTrue(es.contains(e12));
        assertTrue(es.contains(e45));
    }

    /* ====================== Neighbours & incident edges ====================== */

    @Test
    public void testGetEdgeAndGetEdgesOfVertex() {
        Edge<Vertex> e = g.getEdge(v1, v3);
        assertNotNull(e);
        assertEquals(e13, e);

        Set<Edge<Vertex>> inc1 = g.getEdges(v1);
        assertEquals(2, inc1.size()); // v1 connected to v2, v3
        assertTrue(inc1.contains(e12));
        assertTrue(inc1.contains(e13));
    }

    @Test
    public void testGetNeighbours() {
        Map<Vertex, Edge<Vertex>> nbrs2 = g.getNeighbours(v2);
        assertEquals(3, nbrs2.size());
        assertTrue(nbrs2.containsKey(v1));
        assertTrue(nbrs2.containsKey(v3));
        assertTrue(nbrs2.containsKey(v4));

        // neighbours should map to the matching edges
        assertEquals(e12, nbrs2.get(v1));
        assertEquals(e23, nbrs2.get(v3));
        assertEquals(e24, nbrs2.get(v4));
    }

    /* ====================== Algorithms.pathCost ====================== */

    @Test
    public void testPathCostSumEdges() {
        List<Vertex> path = Arrays.asList(v1, v2, v3, v4);
        double cost = g.pathCost(path, PathCostType.SUM_EDGES);
        // 1 + 1 + 3 = 5
        assertEquals(5.0, cost, 1e-9);
    }

    @Test
    public void testPathCostMaxEdge() {
        List<Vertex> path = Arrays.asList(v1, v2, v4, v5);
        double cost = g.pathCost(path, PathCostType.MAX_EDGE);
        // edges: 1.0, 2.5, 1.5 -> max = 2.5
        assertEquals(2.5, cost, 1e-9);
    }

    @Test
    public void testPathCostTrivialPath() {
        // size 0
        assertEquals(0.0, g.pathCost(Collections.emptyList(), PathCostType.SUM_EDGES));
        // size 1
        assertEquals(0.0, g.pathCost(Collections.singletonList(v1), PathCostType.MAX_EDGE));
    }



    @Test
    public void testMinimumCostPathSumEdgesChoosesCheapestSum() {
        // For SUM_EDGES:
        //  v1 -> v2 -> v3 has cost 1 + 1 = 2
        //  v1 -> v3 directly has cost 5
        List<Vertex> path = g.minimumCostPath(v1, v3, PathCostType.SUM_EDGES);
        assertEquals(Arrays.asList(v1, v2, v3), path);
        assertEquals(2.0, g.pathCost(path, PathCostType.SUM_EDGES), 1e-9);
    }

    @Test
    public void testMinimumCostPathMaxEdgeChoosesBetterBottleneck() {
        // For MAX_EDGE:
        //  current weights: v1-2 (1), v2-3 (1), v1-3 (5)
        //  bottleneck(v1-2-3) = max(1,1) = 1
        //  bottleneck(v1-3)   = 5
        // So minimax path is still v1-2-3
        List<Vertex> path = g.minimumCostPath(v1, v3, PathCostType.MAX_EDGE);
        assertEquals(Arrays.asList(v1, v2, v3), path);
        assertEquals(1.0, g.pathCost(path, PathCostType.MAX_EDGE), 1e-9);
    }

    @Test
    public void testMinimumCostPathNoPathBetweenComponents() {
        // build a graph with a disconnected vertex
        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5));
        Set<Edge<Vertex>> es = new HashSet<>(Arrays.asList(e12)); // only v1-v2 connected
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g2 =
                new AdjacencyMatrixGraph<>(vs, es);

        List<Vertex> path = g2.minimumCostPath(v1, v5, PathCostType.SUM_EDGES);
        assertTrue(path.isEmpty());
    }


    @Test
    public void testDiameterAndCenterOnLineGraph() {
        // line graph: 1-2-3
        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2, v3));
        Edge<Vertex> e12line = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> e23line = new Edge<>(v2, v3, 1.0);
        Set<Edge<Vertex>> es = new HashSet<>(Arrays.asList(e12line, e23line));
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> lineG =
                new AdjacencyMatrixGraph<>(vs, es);

        double dia = lineG.getDiameter(PathCostType.SUM_EDGES);
        // diameter is dist(1,3) = 2
        assertEquals(2.0, dia, 1e-9);

        Vertex center = lineG.getCenter(PathCostType.SUM_EDGES);
        // center should be the middle vertex
        assertEquals(v2, center);
    }

    @Test
    public void testCenterOnDisconnectedGraphChoosesLargestComponent() {
        // components: {1-2-3} and {4-5}, largest is {1,2,3}
        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2, v3, v4, v5));
        Set<Edge<Vertex>> es = new HashSet<>(Arrays.asList(
                e12, e23,        // component 1
                e45              // component 2
        ));
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g2 =
                new AdjacencyMatrixGraph<>(vs, es);

        Vertex center = g2.getCenter(PathCostType.SUM_EDGES);
        assertNotNull(center);
        // center must belong to the largest component {1,2,3}
        assertTrue(center.equals(v1) || center.equals(v2) || center.equals(v3));
    }

    @Test
    public void testDiameterOnSingletonGraph() {
        Set<Vertex> vs = new HashSet<>(Collections.singletonList(v1));
        Set<Edge<Vertex>> es = new HashSet<>();
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g2 =
                new AdjacencyMatrixGraph<>(vs, es);

        assertEquals(0.0, g2.getDiameter(PathCostType.SUM_EDGES), 1e-9);
    }

    @Test
    public void testDiameterOnEmptyGraph() {
        Set<Vertex> vs = new HashSet<>();
        Set<Edge<Vertex>> es = new HashSet<>();
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g2 =
                new AdjacencyMatrixGraph<>(vs, es);


        assertEquals(0.0, g2.getDiameter(PathCostType.SUM_EDGES), 1e-9);
    }


    @Test
    public void testGetComponentsThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> g.getComponents());
    }

}