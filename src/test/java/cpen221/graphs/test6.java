package cpen221.graphs;

import cpen221.graphs.graph.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Extra coverage tests for remaining red/yellow lines:
 *  - getComponents() UnsupportedOperationException in both graph classes
 *  - getEdges / getNeighbours null-or-missing-vertex branches
 *  - several defensive checks inside Algorithms.dijkstraLike (via minimumCostPath)
 */
public class test6 {



    @Test
    public void testAdjListGetComponentsThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        assertThrows(UnsupportedOperationException.class, g::getComponents);
    }

    @Test
    public void testAdjMatrixGetComponentsThrows() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);

        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2));
        Set<Edge<Vertex>> es = new HashSet<>(Collections.singletonList(e12));
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g =
                new AdjacencyMatrixGraph<>(vs, es);

        assertThrows(UnsupportedOperationException.class, g::getComponents);
    }


    @Test
    public void testAdjListGetEdgesAndNeighboursOnMissingVertex() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(new Edge<>(v1, v2, 1.0));

        Vertex vMissing = new Vertex(99);

        assertTrue(g.getEdges(vMissing).isEmpty());
        assertTrue(g.getNeighbours(vMissing).isEmpty());


        assertTrue(g.getEdges(null).isEmpty());
        assertTrue(g.getNeighbours(null).isEmpty());
    }


    @Test
    public void testAdjMatrixGetEdgesAndNeighboursOnMissingVertex() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Edge<Vertex> e12 = new Edge<>(v1, v2, 1.0);

        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2));
        Set<Edge<Vertex>> es = new HashSet<>(Collections.singletonList(e12));
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g =
                new AdjacencyMatrixGraph<>(vs, es);

        Vertex vMissing = new Vertex(99);


        assertTrue(g.getEdges(vMissing).isEmpty());
        assertTrue(g.getNeighbours(vMissing).isEmpty());


        assertTrue(g.getEdges(null).isEmpty());
        assertTrue(g.getNeighbours(null).isEmpty());
    }


    private static class BadGraphNullInDomain extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        @Override
        public Set<Vertex> getVertices() {
            Set<Vertex> base = super.getVertices();
            base.add(null);
            return base;
        }
    }

    @Test
    public void testDijkstraDomainContainsNullVertexThrows() {
        BadGraphNullInDomain g = new BadGraphNullInDomain();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);


        assertThrows(NullPointerException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }

    private static class BadGraphForeignInDomain extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        private final Vertex ghost = new Vertex(9999);

        @Override
        public Set<Vertex> getVertices() {
            Set<Vertex> base = super.getVertices();
            base.add(ghost);
            return base;
        }
    }

    @Test
    public void testDijkstraDomainContainsForeignVertexThrows() {
        BadGraphForeignInDomain g = new BadGraphForeignInDomain();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }


    private static class BadGraphNullNeighbours extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        @Override
        public Map<Vertex, Edge<Vertex>> getNeighbours(Vertex v) {
            return null;
        }
    }

    @Test
    public void testDijkstraGetNeighboursReturnsNullThrows() {
        BadGraphNullNeighbours g = new BadGraphNullNeighbours();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(new Edge<>(v1, v2, 1.0));

        assertThrows(IllegalStateException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }
    @Test
    public void testAdjListEdgeLengthNoSuchEdgeThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        assertThrows(NoSuchElementException.class, () -> g.edgeLength(v1, v2));
    }

    @Test
    public void testMatrixEdgeLengthNoSuchEdgeThrows() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2));
        Set<Edge<Vertex>> es = new HashSet<>();
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g =
                new AdjacencyMatrixGraph<>(vs, es);

        assertThrows(NoSuchElementException.class, () -> g.edgeLength(v1, v2));
    }



    @Test
    public void testMatrixGetEdgeMissingVertexReturnsNull() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Set<Vertex> vs = new HashSet<>(Arrays.asList(v1, v2));
        Set<Edge<Vertex>> es = new HashSet<>();
        AdjacencyMatrixGraph<Vertex, Edge<Vertex>> g =
                new AdjacencyMatrixGraph<>(vs, es);

        assertNull(g.getEdge(v1, v3));
        assertNull(g.getEdge(v3, v2));
    }



    @Test
    public void testAdjListRemoveEdgeByVerticesNoEdgeReturnsFalse() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        assertFalse(g.removeEdge(v1, v2));
    }


    private static class BadGraphNullVertices extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        @Override
        public Set<Vertex> getVertices() {
            return null;
        }
    }

    @Test
    public void testDijkstraNullDomainThrows() {
        BadGraphNullVertices g = new BadGraphNullVertices();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);

        assertThrows(NullPointerException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }


    private static class BadGraphNullNeighbour extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        @Override
        public Map<Vertex, Edge<Vertex>> getNeighbours(Vertex v) {
            Map<Vertex, Edge<Vertex>> base = super.getNeighbours(v);
            if (base.isEmpty()) {
                return base;
            }
            Map<Vertex, Edge<Vertex>> bad = new HashMap<>();

            Edge<Vertex> e = base.values().iterator().next();
            bad.put(null, e);
            return bad;
        }
    }




    private static class BadGraphNeighbourMissingEdge extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        @Override
        public boolean hasEdge(Vertex v1, Vertex v2) {

            return false;
        }
    }

    @Test
    public void testDijkstraNeighbourMissingEdgeThrows() {
        BadGraphNeighbourMissingEdge g = new BadGraphNeighbourMissingEdge();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(new Edge<>(v1, v2, 1.0));   // 真有边，但 hasEdge 会撒谎

        assertThrows(IllegalStateException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }

    private static class BadEdge extends Edge<Vertex> {
        private final double fakeLen;

        public BadEdge(Vertex v1, Vertex v2, double fakeLen) {
            super(v1, v2, 1.0);
            this.fakeLen = fakeLen;
        }

        @Override
        public double length() {
            return fakeLen;
        }
    }

    @Test
    public void testDijkstraInvalidEdgeLengthThrows() {
        AdjacencyListGraph<Vertex, Edge<Vertex>> g = new AdjacencyListGraph<>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        Edge<Vertex> bad = new BadEdge(v1, v2, -1.0); // 运行期返回负长度
        g.addEdge(bad);

        assertThrows(IllegalArgumentException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }


    private static class GhostSet<E> extends AbstractSet<E> {
        private final Set<E> base;
        private final E ghost;

        GhostSet(Set<E> base, E ghost) {
            this.base = base;
            this.ghost = ghost;
        }

        @Override
        public Iterator<E> iterator() {
            return base.iterator();
        }

        @Override
        public int size() {
            return base.size();
        }

        @Override
        public boolean contains(Object o) {
            return base.contains(o) || o == ghost;
        }
    }

    private static class BadGraphDistMissing extends AdjacencyListGraph<Vertex, Edge<Vertex>> {
        private final Vertex ghost = new Vertex(9999);

        @Override
        public Set<Vertex> getVertices() {
            return new GhostSet<>(super.getVertices(), ghost);
        }

        @Override
        public Map<Vertex, Edge<Vertex>> getNeighbours(Vertex v) {
            Map<Vertex, Edge<Vertex>> base = super.getNeighbours(v);
            if (base.isEmpty()) return base;
            Map<Vertex, Edge<Vertex>> result = new HashMap<>(base);

            Edge<Vertex> fake = new Edge<>(v, ghost, 1.0);
            result.put(ghost, fake);
            return result;
        }

        @Override
        public boolean hasVertex(Vertex v) {
            return super.hasVertex(v) || v.equals(ghost);
        }

        @Override
        public boolean hasEdge(Vertex v1, Vertex v2) {
            if (v2.equals(ghost)) {
                return true;
            }
            return super.hasEdge(v1, v2);
        }
    }

    @Test
    public void testDijkstraDistMissingVertexThrows() {
        BadGraphDistMissing g = new BadGraphDistMissing();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(new Edge<>(v1, v2, 1.0));

        assertThrows(IllegalStateException.class,
                () -> Algorithms.minimumCostPath(g, v1, v2, PathCostType.SUM_EDGES));
    }

}