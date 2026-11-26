package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extra branch-coverage tests on top of existing ones:
 *  - more AdjacencyListGraph branches (duplicates, non-existent removals, invalid paths)
 *  - AdjacencyMatrixGraph.edgeLengthSum
 *  - Edge equals / distinctVertex extra branches
 */
public class test5 {

    private AdjacencyListGraph<Vertex, Edge<Vertex>> listGraph;
    private AdjacencyMatrixGraph<Vertex, Edge<Vertex>> matrixGraph;

    private Vertex v1, v2, v3, v4;
    private Edge<Vertex> e12, e23, e34, e13;

    @BeforeEach
    public void setup() {
        v1 = new Vertex(1);
        v2 = new Vertex(2);
        v3 = new Vertex(3);
        v4 = new Vertex(4);

        // weights:
        // 1–2:1, 2–3:1, 3–4:2, 1–3:5
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


        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2, v3, v4));
        Set<Edge<Vertex>> es = new HashSet<>(Arrays.asList(e12, e23, e34));
        matrixGraph = new AdjacencyMatrixGraph<>(vs, es);
    }



    @Test
    public void testListDuplicateAddVertexAndHasEdgeNull() {
        // duplicate addVertex 分支
        assertFalse(listGraph.addVertex(v1));  // v1 已经在图里

        // hasEdge(null) 分支
        assertFalse(listGraph.hasEdge((Edge<Vertex>) null));
    }

    @Test
    public void testListRemoveNonexistentVertexAndEdge() {
        Vertex v99 = new Vertex(99);
        Edge<Vertex> fake = new Edge<>(v1, v4, 10.0);


        assertFalse(listGraph.removeVertex(v99));


        assertFalse(listGraph.removeEdge(fake));


        assertFalse(listGraph.removeEdge(v1, v4));
    }

    @Test
    public void testListPathCostInvalidPathThrows() {
        // 路径里包含一条不存在的边 (v1->v4)
        List<Vertex> invalid = Arrays.asList(v1, v4);

        assertThrows(IllegalArgumentException.class, () ->
                listGraph.pathCost(invalid, PathCostType.SUM_EDGES));
    }

    @Test
    public void testListMinimumCostPathInvalidArgs() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);


        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.minimumCostPath(g, v2, v1, PathCostType.SUM_EDGES));
    }

    @Test
    public void testListEdgeLengthSum() {

        double sum = listGraph.edgeLengthSum();
        assertEquals(9.0, sum, 1e-9);
    }


    @Test
    public void testMatrixEdgeLengthSum() {

        double sum = matrixGraph.edgeLengthSum();
        assertEquals(4.0, sum, 1e-9);
    }


    @Test
    public void testEdgeEqualsWithNullAndDifferentObjects() {
        Edge<Vertex> otherSame = new Edge<>(v1, v2, 1.0);
        Edge<Vertex> otherDiffWeight = new Edge<>(v1, v2, 2.0);
        Edge<Vertex> otherDiffEndpoints = new Edge<>(v2, v3, 1.0);


        assertEquals(e12, e12);


        assertEquals(e12, otherSame);


        assertEquals(e12, otherDiffWeight);


        assertNotEquals(e12, null);
        assertNotEquals(e12, "not an edge");


        assertNotEquals(e12, otherDiffEndpoints);
    }

    @Test
    public void testEdgeDistinctVertexNoIntersectionBranch() {

        Vertex dv = e12.distinctVertex(e34);


        assertNotNull(dv);
        assertTrue(dv.equals(v1) || dv.equals(v2));
    }
}