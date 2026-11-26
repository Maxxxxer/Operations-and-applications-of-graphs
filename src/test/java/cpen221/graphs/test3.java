package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//
public class test3 {

    private AdjacencyListGraph<Vertex, Edge<Vertex>> listGraph;
    private Vertex v1, v2, v3, v4;
    private Edge<Vertex> e12, e23, e34, e13;

    @BeforeEach
    public void setup() {
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
        v4 = new Vertex(4);

        // weights carefully chosen:
        //   v1-2 = 1, v2-3 = 1, v1-3 = 5, v3-4 = 2
        e12 = new Edge<>(v1, v2, 1.0);
        e23 = new Edge<>(v2, v3, 1.0);
        e34 = new Edge<>(v3, v4, 2.0);
        e13 = new Edge<>(v1, v3, 5.0);

        listGraph = new AdjacencyListGraph<>();
        listGraph.addVertex(v1);
        listGraph.addVertex(v2);
        listGraph.addVertex(v3);
        listGraph.addVertex(v4);

        listGraph.addEdge(e12);
        listGraph.addEdge(e23);
        listGraph.addEdge(e34);
        listGraph.addEdge(e13);
    }



    @Test
    public void testListAddAndHasVertexAndEdge() {
        // vertices
        assertTrue(listGraph.hasVertex(v1));
        assertTrue(listGraph.hasVertex(v4));
        assertFalse(listGraph.hasVertex(new Vertex(99)));

        // edges
        assertTrue(listGraph.hasEdge(e12));
        assertTrue(listGraph.hasEdge(v1, v2));
        assertTrue(listGraph.hasEdge(v2, v1)); // undirected

        assertFalse(listGraph.hasEdge(v1, v4));
    }

    @Test
    public void testListGetVerticesAndEdges() {
        Set<Vertex> vs = listGraph.getVertices();
        Set<Edge<Vertex>> es = listGraph.getEdges();

        assertEquals(4, vs.size());
        assertTrue(vs.contains(v1));
        assertTrue(vs.contains(v4));

        // we added 4 edges
        assertEquals(4, es.size());
        assertTrue(es.contains(e12));
        assertTrue(es.contains(e34));
    }

    @Test
    public void testListGetEdgeAndEdgeLengthAndIncidentEdges() {
        // getEdge and edgeLength
        Edge<Vertex> e = listGraph.getEdge(v1, v3);
        assertEquals(e13, e);

        double len = listGraph.edgeLength(v3, v4);
        assertEquals(2.0, len, 1e-9);

        // getEdges(v)
        Set<Edge<Vertex>> inc = listGraph.getEdges(v2);
        assertEquals(2, inc.size());
        assertTrue(inc.contains(e12));
        assertTrue(inc.contains(e23));
    }

    @Test
    public void testListGetNeighboursAndRemoveEdgeAndVertex() {
        // neighbours of v2: v1 and v3
        Map<Vertex, Edge<Vertex>> nbrs = listGraph.getNeighbours(v2);
        assertEquals(2, nbrs.size());
        assertEquals(e12, nbrs.get(v1));
        assertEquals(e23, nbrs.get(v3));

        // remove one edge
        assertTrue(listGraph.removeEdge(e12));
        assertFalse(listGraph.hasEdge(v1, v2));

        // remove vertex v4 and its incident edges
        assertTrue(listGraph.removeVertex(v4));
        assertFalse(listGraph.hasVertex(v4));
        assertFalse(listGraph.hasEdge(v3, v4));
    }


    @Test
    public void testListPathCostSumAndMax() {
        List<Vertex> path = Arrays.asList(v1, v2, v3, v4);

        double sumCost = listGraph.pathCost(path, PathCostType.SUM_EDGES);
        // 1 + 1 + 2 = 4
        assertEquals(4.0, sumCost, 1e-9);

        double maxCost = listGraph.pathCost(path, PathCostType.MAX_EDGE);
        // edges: 1,1,2 -> max = 2
        assertEquals(2.0, maxCost, 1e-9);
    }

    @Test
    public void testListMinimumCostPathSumEdges() {
        // for SUM_EDGES:
        //  v1->v2->v3 cost 1+1 = 2
        //  v1->v3 direct cost = 5
        List<Vertex> path = listGraph.minimumCostPath(v1, v3, PathCostType.SUM_EDGES);
        assertEquals(Arrays.asList(v1, v2, v3), path);
    }

    @Test
    public void testListMinimumCostPathMaxEdge() {
        // for MAX_EDGE with current weights:
        //  via v2: edges 1,1 -> bottleneck 1
        //  direct: 5              -> bottleneck 5
        List<Vertex> path = listGraph.minimumCostPath(v1, v3, PathCostType.MAX_EDGE);
        assertEquals(Arrays.asList(v1, v2, v3), path);

        // and verify cost computed through listGraph -> Algorithms.pathCost
        double cost = listGraph.pathCost(path, PathCostType.MAX_EDGE);
        assertEquals(1.0, cost, 1e-9);
    }

    @Test
    public void testListDiameterAndCenter() {
        // Using SUM_EDGES
        double dia = listGraph.getDiameter(PathCostType.SUM_EDGES);
        // rough check: diameter at least distance v1-v4 = path 1-2-3-4 (cost 4)
        assertTrue(dia >= 4.0);

        Vertex center = listGraph.getCenter(PathCostType.SUM_EDGES);
        assertNotNull(center);
        // in this chain-like graph, center should be one of {v2, v3}
        assertTrue(center.equals(v2) || center.equals(v3));

        // Also call with MAX_EDGE metric to hit the other branch in Algorithms
        double diaMax = listGraph.getDiameter(PathCostType.MAX_EDGE);
        assertTrue(diaMax >= 2.0);
        Vertex centerMax = listGraph.getCenter(PathCostType.MAX_EDGE);
        assertNotNull(centerMax);
    }


    @Test
    public void testEdgeCanonicalOrderingAndEqualsHashCode() {
        // construct with reversed endpoints; should canonicalize to (1,2)
        Edge<Vertex> eReversed = new Edge<>(v2, v1, 1.0);

        assertEquals(e12.v1(), eReversed.v1());
        assertEquals(e12.v2(), eReversed.v2());
        assertEquals(e12.length(), eReversed.length(), 1e-9);

        assertEquals(e12, eReversed);
        assertEquals(e12.hashCode(), eReversed.hashCode());
    }

    @Test
    public void testEdgeIncidentAndIntersects() {
        assertTrue(e12.incident(v1));
        assertTrue(e12.incident(v2));
        assertFalse(e12.incident(v3));

        assertTrue(e12.intersects(e23));   // share v2

        // use two edges with disjoint endpoints: (1,2) and (3,4)
        assertFalse(e12.intersects(e34));
    }

    @Test
    public void testEdgeIntersectionAndDistinctVertex() {
        // intersection of e12 and e23 is v2
        Vertex inter = e12.intersection(e23);
        assertEquals(v2, inter);

        // distinctVertex(v1) on e12 -> v2
        assertEquals(v2, e12.distinctVertex(v1));
        // distinctVertex(v2) on e12 -> v1
        assertEquals(v1, e12.distinctVertex(v2));

        // distinctVertex(e23) on e12 should give the endpoint that is not the shared one
        Vertex dv = e12.distinctVertex(e23);
        assertTrue(dv.equals(v1)); // e12 endpoints {v1,v2}, shared = v2
    }

    /* ====================== Vertex equality / hashCode ====================== */

    @Test
    public void testVertexEqualityAndHash() {
        Vertex a = new Vertex(10);
        Vertex b = new Vertex(10);
        Vertex c = new Vertex(11);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(b, c);
    }
}